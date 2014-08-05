/**
 * 
 */
package com.wwidesigner.modelling;

import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.wwidesigner.modelling.PlayingRange.NoPlayingRange;
import com.wwidesigner.note.Fingering;
import com.wwidesigner.note.Note;
import com.wwidesigner.note.Tuning;
import com.wwidesigner.util.PhysicalParameters;

/**
 * InstrumentTuner for calculators that predict minimum and maximum
 * frequencies of a playing range.  Predicts nominal frequency
 * from a nominal playing pattern of an instrument
 * (how the player would expect to play each note).
 *
 * For the nominal playing pattern, we use a linear change in
 * blowing velocity from just below fmax for the lowest note,
 * to somewhat above fmin for the highest note.  
 * This is only *one* possible playing pattern, and has not yet been
 * validated against the playing of real players.
 * cf. Fletcher and Rossing, The physics of musical instruments, 2nd ed.,
 * New York: Springer, 2010, section 16.10 and figure 16.23.
 * 
 * 
 * @author Burton Patkau
 */
public class LinearVInstrumentTuner extends InstrumentTuner
{
	// Target velocity of lowest note is less than velocity at fmax
	// by BottomFraction of the velocity difference between fmax and fmin.
	// Target velocity of highest note is less than velocity at fmax
	// by TopFraction of the velocity difference between fmax and fmin.
	protected double BottomFraction;
	protected double TopFraction;

	// Standard ranges for BottomFraction and TopFraction.
	// Evaluator uses blowing level, 0 .. 10, to interpolate between these ranges,
	// using the Lo value at blowing level 0 and the Hi value at blowing level 10.
	// Default fractions are the average of Hi and Lo values, blowing level 5.
	protected static final double BottomLo = 0.20;
	protected static final double BottomHi = 0.05;
	protected static final double TopLo = 0.90;
	protected static final double TopHi = 0.30;

	protected double fLow;		// Lowest frequency in target range.
	protected double fHigh;		// Highest frequency in target range.
	// Linear equation parameters for calculating nominal impedance:
	// Vnom = slope * f + intercept.
	protected double slope;
	protected double intercept;

	public LinearVInstrumentTuner()
	{
		this(5);
	}
	
	public LinearVInstrumentTuner(int blowingLevel)
	{
		// Interpolate between Low and Hi values, depending on blowing level.
		// For bottom note, we want to stick close to BottomHi, except at
		// the lowest blowing levels.
		// For top note, we want to stick close to BottomLo, except at
		// the highest blowing levels.
		// For top note, we use linear interpolation between TopLow and TopHi.
		this(BottomHi - (double)((10-blowingLevel)*(10-blowingLevel)) * 0.01 * (BottomHi - BottomLo),
			 TopLo + (double)(blowingLevel*blowingLevel) * 0.01 * (TopHi - TopLo));
	}
	
	public LinearVInstrumentTuner(double bottomFr, double topFr)
	{
		super();
		BottomFraction = bottomFr;
		TopFraction    = topFr;
		fLow           = 100.0;
		fHigh          = 100.0;
		slope          = 0.0;
		intercept      = 0.0;
	}

	// Complementary functions for velocity estimation.

	/**
	 * Estimate the average velocity of air leaving the windway. 
	 * @param f - Actual playing frequency, in Hz.
	 * @param windowLength - Length of window, in meters.
	 * @param z - Total impedance of whistle.
	 * @return Estimated average air velocity leaving windway, in m/s.
	 */
	public static double velocity(double f, double windowLength, Complex z)
	{
		return f * windowLength / (0.26 - 0.037 * z.getImaginary()/z.getReal());
	}

	/**
	 * Estimate the expected ratio Im(z)/Re(z) for a given air velocity. 
	 * @param f - Playing frequency, in Hz.
	 * @param windowLength - Length of window, in meters.
	 * @param velocity - Average air velocity leaving windway, in m/s.
	 * @return Predicted ratio Im(z)/Re(z).
	 */
	public static double zRatio(double f, double windowLength, double velocity)
	{
		return (0.26 - f * windowLength / velocity )/0.037;
	}

	/**
	 * Set interpolation parameters to interpolate velocity
	 * for a specified set of fingering targets.
	 * Following this call, use getNominalV() to return interpolated velocity.
	 * @param fingeringTargets
	 */
	public void setFingering(List<Fingering> fingeringTargets)
	{
		// Get lowest and highest target notes, and estimate a target velocity for each.

		// Target frequencies for lowest and highest note,
		// then the nominal frequency for these notes, used in
		// linear interpolation of velocity.
		fLow  = 100000.0;
		fHigh = 0.0;
		// Target velocity for lowest and highest notes.
		double vLow  = 0.0;
		double vHigh = 0.0;
		double windowLength = calculator.getInstrument().getMouthpiece().getFipple().getWindowLength();

		// Find lowest and highest target notes.

		Fingering noteLow = new Fingering();
		noteLow.setNote(new Note());
		Fingering noteHigh = new Fingering();
		noteHigh.setNote(new Note());
		for (Fingering target: fingeringTargets)
		{
			if ( target.getNote() != null )
			{
				if( target.getNote().getFrequency() != null )
				{
					if ( target.getNote().getFrequency() < fLow )
					{
						fLow = target.getNote().getFrequency();
						noteLow.setOpenHole(target.getOpenHole());
					}
					if ( target.getNote().getFrequency() > fHigh )
					{
						fHigh = target.getNote().getFrequency();
						noteHigh.setOpenHole(target.getOpenHole());
					}
				}
				else if( target.getNote().getFrequencyMax() != null )
				{
					// If we don't have a nominal frequency, look for fmax.
					if ( target.getNote().getFrequencyMax() < fLow )
					{
						fLow = target.getNote().getFrequencyMax();
						noteLow.setOpenHole(target.getOpenHole());
					}
					if ( target.getNote().getFrequencyMax() > fHigh )
					{
						fHigh = target.getNote().getFrequencyMax();
						noteHigh.setOpenHole(target.getOpenHole());
					}
				}
			}
		}
		
		noteLow.getNote().setFrequency(fLow);
		noteHigh.getNote().setFrequency(fHigh);
		
		// Locate playing ranges at fLow and fHigh,
		// and calculate nominal velocity at these frequencies.
		
		double fmax, fmin;

		calculator.setFingering(noteLow);
		PlayingRange range = new PlayingRange(calculator);
		try 
		{
			fmax = range.findXZero(fLow);
			fmin = range.findFmin(fmax);
			double vMax = velocity(fmax,windowLength,calculator.calcZ(fmax));
			double vMin = velocity(fmin,windowLength,calculator.calcZ(fmin));
			vLow = vMax - BottomFraction * (vMax - vMin);
		}
		catch ( NoPlayingRange e )
		{
			fmax = fmin = fLow;
			// Use predicted velocity if fLow is fmax, at which Im(Z)=0.
			vLow =  velocity(fLow,windowLength,Complex.ONE);
		}
		fLow = fmax;	// Nominal frequency for our interpolation.
		
		calculator.setFingering(noteHigh);
		try
		{
			fmax = range.findXZero(fHigh);
			fmin = range.findFmin(fmax);
			double vMax = velocity(fmax,windowLength,calculator.calcZ(fmax));
			double vMin = velocity(fmin,windowLength,calculator.calcZ(fmin));
			vHigh = vMax - TopFraction * (vMax - vMin);
		}
		catch ( NoPlayingRange e )
		{
			fmax = fmin = fHigh;
			// Use predicted velocity if fHigh is fmax, at which Im(Z)=0.
			vHigh =  velocity(fHigh,windowLength,Complex.ONE);
		}
		fHigh = fmax;	// Nominal frequency for our interpolation.
		
		// Nominal velocity is a linear interpolation between (fLow,imagLow) and (fHigh,imagHigh),
		// imagNom = slope * frequency + intercept.
		slope = (vHigh - vLow)/(fHigh - fLow);
		intercept = vLow - slope * fLow;
	}
	
	/**
	 * Following a call to setFingering(), return interpolated velocity.
	 * @param f - frequency
	 * @return nominal velocity at specified frequency
	 */
	public double getNominalV(double f)
	{
		return slope * f + intercept;
	}

	/* (non-Javadoc)
	 * @see com.wwidesigner.modelling.InstrumentTuner#setTuning(com.wwidesigner.note.Tuning)
	 */
	@Override
	public void setTuning(Tuning tuning)
	{
		super.setTuning(tuning);
		if (tuning != null && calculator != null)
		{
			setFingering(tuning.getFingering());
		}
	}

	/* (non-Javadoc)
	 * @see com.wwidesigner.modelling.InstrumentTuner#setCalculator(com.wwidesigner.modelling.InstrumentCalculator)
	 */
	@Override
	public void setCalculator(InstrumentCalculator calculator)
	{
		super.setCalculator(calculator);
		if (tuning != null && calculator != null)
		{
			setFingering(tuning.getFingering());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wwidesigner.modelling.InstrumentTuner#setParams(com.wwidesigner.util.PhysicalParameters)
	 */
	@Override
	public void setParams(PhysicalParameters params)
	{
		super.setParams(params);
		if (tuning != null && calculator != null)
		{
			setFingering(tuning.getFingering());
		}
	}
	

	/* (non-Javadoc)
	 * @see com.wwidesigner.modelling.InstrumentTuner#predictedFrequency(com.wwidesigner.note.Fingering)
	 */
	@Override
	public Double predictedFrequency(Fingering fingering)
	{
		Note targetNote = fingering.getNote();
		PlayingRange range = new PlayingRange(calculator, fingering);
		try
		{
			double target = getFrequencyTarget(targetNote);
			double windowLength = calculator.getInstrument().getMouthpiece().getFipple().getWindowLength();
			double zRatio = zRatio(target, windowLength, getNominalV(target));
			return range.findZRatio(target, zRatio);
		}
		catch (NoPlayingRange e)
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wwidesigner.modelling.InstrumentTuner#predictedNote(com.wwidesigner.note.Fingering)
	 */
	@Override
	public Note predictedNote(Fingering fingering)
	{
		Note targetNote = fingering.getNote();
		Note predNote = new Note();
		predNote.setName(targetNote.getName());
		if ( targetNote.getFrequency() != null )
		{
			predNote.setFrequency(targetNote.getFrequency());
		}
		double target = getFrequencyTarget(targetNote);
		
		if (target == 0.0)
		{
			// No target frequency.
			// Return note without prediction, because we can't make a prediction.
			return predNote;
		}

		// Predict playing range.
		PlayingRange range = new PlayingRange(calculator, fingering);
		double fmax, fmin, fnom;
		try {
			fmax = range.findXZero(target);
			predNote.setFrequencyMax(fmax);
			fmin = range.findFmin(fmax);
			predNote.setFrequencyMin(fmin);
		}
		catch (NoPlayingRange e)
		{
			// Leave fmax and fmin unassigned.
		}
		try {
			double windowLength = calculator.getInstrument().getMouthpiece().getFipple().getWindowLength();
			double zRatio = zRatio(target, windowLength, getNominalV(target));
			fnom = range.findZRatio(target, zRatio);
			predNote.setFrequency(fnom);
		}
		catch (NoPlayingRange e)
		{
			// Leave fnom unassigned.
		}
		return predNote;
	}
}