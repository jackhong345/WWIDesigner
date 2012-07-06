/**
 * 
 */
package com.wwidesigner.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.math3.complex.Complex;

import com.wwidesigner.math.StateVector;
import com.wwidesigner.math.TransferMatrix;
import com.wwidesigner.note.bind.Fingering;
import com.wwidesigner.util.PhysicalParameters;

/**
 * @author kort
 * 
 */
public class Instrument implements InstrumentInterface
{

	protected String name;
	protected LengthType lengthType;
	protected Mouthpiece mouthpiece;
	protected List<BorePoint> borePoint;
	protected String description;
	protected List<Hole> hole;
	protected List<ComponentInterface> components;
	protected Termination termination;

	public Instrument()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wwidesigner.geometry.InstrumentInterface#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wwidesigner.geometry.InstrumentInterface#setName(java.lang.String)
	 */
	@Override
	public void setName(String value)
	{
		this.name = value;
	}

	/**
	 * @return the lengthType
	 */
	public LengthType getLengthType()
	{
		return lengthType;
	}

	/**
	 * @param lengthType
	 *            the lengthType to set
	 */
	public void setLengthType(LengthType lengthType)
	{
		this.lengthType = lengthType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wwidesigner.geometry.InstrumentInterface#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wwidesigner.geometry.InstrumentInterface#setDescription(java.lang
	 * .String)
	 */
	@Override
	public void setDescription(String value)
	{
		description = value;
	}

	/**
	 * @param borePoint
	 *            the borePoint to set
	 */
	public void setBorePoint(List<BorePoint> borePoint)
	{
		this.borePoint = borePoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wwidesigner.geometry.InstrumentInterface#getBorePoint()
	 */
	@Override
	public List<BorePoint> getBorePoint()
	{
		return borePoint;
	}

	@Override
	public void addBorePoint(BorePoint borePoint)
	{
		getBorePoint().add(borePoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wwidesigner.geometry.InstrumentInterface#getMouthpiece()
	 */
	@Override
	public Mouthpiece getMouthpiece()
	{
		return mouthpiece;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wwidesigner.geometry.InstrumentInterface#setMouthpiece(com.wwidesigner
	 * .geometry.bind.XmlMouthpiece)
	 */
	@Override
	public void setMouthpiece(Mouthpiece value)
	{
		mouthpiece = value;
	}

	/**
	 * @param hole
	 *            the hole to set
	 */
	public void setHole(List<Hole> hole)
	{
		this.hole = hole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wwidesigner.geometry.InstrumentInterface#getHole()
	 */
	@Override
	public List<Hole> getHole()
	{
		return hole;
	}

	@Override
	public void addHole(Hole hole)
	{
		getHole().add(hole);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wwidesigner.geometry.InstrumentInterface#getTermination()
	 */
	@Override
	public Termination getTermination()
	{
		return termination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wwidesigner.geometry.InstrumentInterface#setTermination(com.wwidesigner
	 * .geometry.bind.XmlEndBoreSection)
	 */
	@Override
	public void setTermination(Termination value)
	{
		termination = value;
	}

	@Override
	public void updateComponents()
	{
		// TODO Write recursive validation method and call it here.
		// Then take out the error checking in all the other methods.
		components = new ArrayList<ComponentInterface>();

		if (borePoint != null && !borePoint.isEmpty())
		{
			SortedMap<Double, BorePoint> borePointMap = makePositionMap(borePoint);
			// Add the mouthpiece reference position to the map.
			// I don't believe the optimization routines should care that this
			// offset
			// is not subtracted, since the calculations are performed on the
			// components, which are offset agnostic.
			// borePointMap.put(mouthpieceOrigin.getBorePosition(),
			
			SortedMap<Double, Hole> holeMap = makePositionMap(hole);

			// TODO Deal with the Mouthpiece and the start of the bore:

			// Process the holes, making sections as needed to include the hole
			// on the right
			if (holeMap.size() > 0)
			{
				for (Map.Entry<Double, Hole> holeEntry : holeMap.entrySet())
				{
					double rightPosition = holeEntry.getKey();
					Hole currentHole = holeEntry.getValue();
					makeSections(borePointMap, rightPosition);
					processHole(borePointMap, currentHole);
				}
			}

			// Process the rest of the sections. There must be at least one
			double lastPosition = borePointMap.lastKey() + 1;
			makeSections(borePointMap, lastPosition);
		}
	}

	protected void processHole(SortedMap<Double, BorePoint> borePointMap,
			Hole currentHole)
	{
		// Update bore radius at hole
		// At this stage, the hole must be between the first and second bore
		// point
		Iterator<BorePoint> points = borePointMap.values().iterator();
		BorePoint leftPoint = points.next();
		BorePoint rightPoint = points.next();

		double leftPosition = leftPoint.getBorePosition();
		double rightPosition = rightPoint.getBorePosition();
		double holePosition = currentHole.getBorePosition();
		double holeRelativePosition = (rightPosition - holePosition)
				/ (rightPosition - leftPosition);

		double leftDiameter = leftPoint.getBoreDiameter();
		double rightDiameter = rightPoint.getBoreDiameter();
		double holeBoreDiameter = leftDiameter + (rightDiameter - leftDiameter)
				* holeRelativePosition;
		currentHole.setDiameter(holeBoreDiameter);

		// Make new bore section
		if (rightPosition > holePosition)
		{
			rightPoint = new BorePoint();
			rightPoint.setBoreDiameter(holeBoreDiameter);
			rightPoint.setBorePosition(holePosition);
			borePointMap.put(holePosition, rightPoint);
		}
		addSection(leftPoint, rightPoint);
		borePointMap.remove(leftPosition);

		components.add(currentHole);
	}

	protected double makeSections(SortedMap<Double, BorePoint> borePointMap,
			double rightPosition)
	{
		SortedMap<Double, BorePoint> unprocessedPoints = borePointMap
				.headMap(rightPosition);
		if (unprocessedPoints.size() > 1)
		{
			Iterator<BorePoint> points = unprocessedPoints.values().iterator();
			BorePoint leftPoint = points.next();
			for (; points.hasNext();)
			{
				BorePoint rightPoint = points.next();
				addSection(leftPoint, rightPoint);
				borePointMap.remove(leftPoint.getBorePosition());
				leftPoint = rightPoint;
			}
		}

		return rightPosition;
	}

	protected void addSection(BorePoint leftPoint, BorePoint rightPoint)
	{
		BoreSection section = new BoreSection();
		section.setLength(rightPoint.getBorePosition() - leftPoint.getBorePosition());
		section.setLeftRadius(leftPoint.getBoreDiameter() / 2);
		section.setRightRadius(rightPoint.getBoreDiameter() / 2);

		components.add(section);
	}

	/**
	 * Create position map of holes or bore points (PositionInterface)
	 */
	public static <P extends PositionInterface> SortedMap<Double, P> makePositionMap(
			List<P> positions)
	{
		SortedMap<Double, P> positionMap = new TreeMap<Double, P>();
		for (P position : positions)
		{
			positionMap.put(position.getBorePosition(), position);
		}

		return positionMap;
	}

	@SuppressWarnings("unchecked")
	public static <P extends PositionInterface> P[] sortList(List<P> positions)
	{
		P[] sortedPositions = (P[]) positions.toArray();
		Arrays.sort(sortedPositions, new Comparator<P>()
		{
			public int compare(P first, P second)
			{
				if (first.getBorePosition() < second.getBorePosition())
				{
					return -1;
				}
				if (first.getBorePosition() > second.getBorePosition())
				{
					return 1;
				}
				return 0;
			}
		});

		return sortedPositions;
	}

	@Override
	public Complex calculateReflectionCoefficient(Fingering fingering,
			PhysicalParameters physicalParams)
	{
		TransferMatrix transferMatrix = TransferMatrix.makeIdentity();

		double frequency = fingering.getNote().getFrequency();
		double waveNumber = 2 * Math.PI * frequency
				/ physicalParams.getSpeedOfSound();

		setOpenHoles(fingering);

		updateComponents();

		for (ComponentInterface component : components)
		{
			transferMatrix = TransferMatrix.multiply(transferMatrix,
					component.calcTransferMatrix(waveNumber, physicalParams));
		}

		StateVector sv = TransferMatrix.multiply(transferMatrix,
				termination.calcStateVector(waveNumber, physicalParams));

		// TODO This mouthpiece calculation will change
		double headRadius = ((BoreSection) components.get(0)).getLeftRadius();
		int reflectanceMultiplier = mouthpiece.calcReflectanceMultiplier();
		double impedance = physicalParams.calcZ0(headRadius);
		Complex reflectance = sv.Reflectance(impedance);

		return reflectance.multiply(reflectanceMultiplier);
	}

	public void setOpenHoles(Fingering fingering)
	{
		List<Boolean> openHoles = fingering.getOpenHole();
		Iterator<Boolean> openHoleIterator = openHoles.iterator();
		for (Hole iHole : hole)
		{
			boolean isOpen = openHoleIterator.next();
			iHole.setOpenHole(isOpen);
		}
	}

	@Override
	public Complex calcZ(double freq)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
