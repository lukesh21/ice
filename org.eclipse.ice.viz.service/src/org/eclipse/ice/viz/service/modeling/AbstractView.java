/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.modeling;

import java.util.ArrayList;

import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateableListener;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionManager;
import org.eclipse.ice.viz.service.datastructures.VizObject.UpdateableSubscriptionType;

/**
 * The view of an AbstractMeshComponent shown to the user. The view is
 * responsible for creating, managing, and updating the datastructure(s) which
 * display the associated AbstractMeshComponent in the view's rendering engine's
 * native data types.
 * 
 * @author Robert Smith
 */
public class AbstractView
		implements IManagedUpdateableListener, IManagedUpdateable {

	/**
	 * The transformation representing the part's intended state. This may not
	 * reflect how the graphics program is currently displaying the part. For
	 * that, see previousTransformation.
	 */
	protected Transformation transformation;

	/**
	 * The last transformation which was applied by the rendering engine.
	 */
	private Transformation previousTransformation;

	/**
	 * The list of listeners observing this object.
	 */
	private ArrayList<IVizUpdateableListener> listeners;

	/**
	 * The listeners registered for updates from this object.
	 */
	protected UpdateableSubscriptionManager updateManager = new UpdateableSubscriptionManager(
			this);

	/**
	 * The default constructor.
	 */
	public AbstractView() {
		// Initialize the class variables
		transformation = new Transformation();
		previousTransformation = new Transformation();
		listeners = new ArrayList<IVizUpdateableListener>();

		// Register as a listener to the part's transformation.
		transformation.register(this);
	}

	/**
	 * Getter function for the part's transformation.
	 * 
	 * @return The part's current transformation.
	 */
	public Transformation getTransformation() {
		return transformation;
	}

	/**
	 * Setter function for the part's transformation.
	 * 
	 * @param newTransformation
	 *            The transformation to apply to this part.
	 */
	public void setTransformation(Transformation newTransformation) {
		transformation = newTransformation;

		// Notify own listeners of the change
		UpdateableSubscriptionType[] eventTypes = {
				UpdateableSubscriptionType.Transformation };
		updateManager.notifyListeners(eventTypes);
	}

	// /**
	// * Notify all listeners of an update.
	// */
	// public void notifyListeners() {
	//
	// // If the listeners are empty, return
	// if (this.listeners == null || this.listeners.isEmpty()) {
	// return;
	// }
	//
	// // Get a reference to self
	// final AbstractView self = this;
	//
	// // // Create a thread object that notifies all listeners
	// //
	// // Thread notifyThread = new Thread() {
	// //
	// // @Override
	// // public void run() {
	// // Loop over all listeners and update them
	// for (int i = 0; i < listeners.size(); i++) {
	// listeners.get(i).update(self);
	// }
	// // }
	// // };
	// //
	// // // Start the thread
	// // notifyThread.start();
	// }

	/**
	 * Getter for the part's previous transformation.
	 * 
	 * @return The last transformation which was fully applied to the part by
	 *         the graphics engine.
	 */
	public Transformation getPreviousTransformation() {
		return previousTransformation;
	}

	/**
	 * Notifies the part that the rendering engine has graphically applied the
	 * newest transformation to it.
	 */
	public void synched() {
		// Update the previous transformation to the part's current status.
		previousTransformation = (Transformation) transformation.clone();
	}

	/**
	 * Creates an object which represents the part's model in a native data type
	 * for the application associated with this view.
	 * 
	 */
	public Object getRepresentation() {
		// Nothing to do.
		return null;
	}

	/**
	 * Refreshes the representation of the model.
	 * 
	 * @param model
	 *            A reference to the view's model
	 */
	public void refresh(AbstractMesh model) {
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#
	 * register(org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void register(IManagedUpdateableListener listener) {
		updateManager.register(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable#
	 * unregister(org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IVizUpdateableListener)
	 */
	@Override
	public void unregister(IManagedUpdateableListener listener) {

		// Remove the listener from the list
		updateManager.unregister(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Check if the objects are the same
		if (this == otherObject) {
			return true;
		}

		// Check that the other object is an abstractview
		if (!(otherObject instanceof AbstractView)) {
			return false;
		}

		// Check that the transformations are equal
		if (!(transformation
				.equals(((AbstractView) otherObject).getTransformation()))) {
			return false;
		}

		// If all checks passed, the objects are equal
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#update(org.eclipse.ice.viz.service.
	 * datastructures.VizObject.IVizUpdateable,
	 * org.eclipse.ice.viz.service.datastructures.VizObject.
	 * UpdateableSubscription[])
	 */
	@Override
	public void update(IManagedUpdateable component,
			UpdateableSubscriptionType[] type) {

		// Pass the update to own listeners
		updateManager.notifyListeners(type);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		// Create a new AbstractView and make it a copy of this
		AbstractView clone = new AbstractView();
		clone.copy(this);

		return clone;
	}

	public void copy(AbstractView otherObject) {

		// Copy the other view's data members
		transformation = (Transformation) otherObject.transformation.clone();
		previousTransformation = (Transformation) otherObject.previousTransformation
				.clone();

		// Notify own listeners of the change
		UpdateableSubscriptionType[] eventTypes = {
				UpdateableSubscriptionType.All };
		updateManager.notifyListeners(eventTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.datastructures.VizObject.
	 * IManagedVizUpdateableListener#getSubscriptions(org.eclipse.ice.viz.
	 * service.datastructures.VizObject.IVizUpdateable)
	 */
	@Override
	public ArrayList<UpdateableSubscriptionType> getSubscriptions(
			IManagedUpdateable source) {
		ArrayList<UpdateableSubscriptionType> types = new ArrayList<UpdateableSubscriptionType>();
		types.add(UpdateableSubscriptionType.All);
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = transformation.hashCode();
		hash += previousTransformation.hashCode();
		return hash;
	}
}
