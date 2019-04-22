/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.collections.impl.list.mutable.FastList;

/** OBject consisting of several meshes */
public class HE_MeshCollection implements Iterable<HE_Mesh> {
	final List<HE_Mesh> meshes;
	Future<HE_MeshCollection> future;
	ExecutorService executor;
	LinkedList<Callable<HE_MeshCollection>> tasks;
	boolean finished;

	/**
	 *
	 */
	public HE_MeshCollection() {
		super();
		meshes = new FastList<HE_Mesh>();
		tasks = new LinkedList<Callable<HE_MeshCollection>>();
		future = null;
		executor = null;
		finished = true;
	}

	public HE_MeshCollection(final HEMC_MultiCreator creator) {
		this();
		meshes.addAll(creator.create().meshes);
	}

	public void createThreaded(final HEMC_MultiCreator creator) {
		if (finished) {
			executor = Executors.newFixedThreadPool(1);
			future = executor.submit(new MultiCreatorThread(creator, this));
			tasks = new LinkedList<Callable<HE_MeshCollection>>();
			finished = false;

		}
	}

	public HE_MeshCollection modify(final HEM_Modifier modifier) {
		if (finished) {
			for (HE_Mesh mesh : meshes) {
				modifier.apply(mesh);
				mesh.clearPrecomputed();
			}
		} else {
			tasks.add(new MultiModifierThread(modifier, this));
		}
		return this;
	}

	public HE_MeshCollection modifyThreaded(final HEM_Modifier modifier) {
		tasks.add(new MultiModifierThread(modifier, this));

		return this;
	}

	public HE_MeshCollection subdivide(final HES_Subdividor subdividor) {
		if (finished) {
			for (HE_Mesh mesh : meshes) {
				subdividor.apply(mesh);
				mesh.clearPrecomputed();
			}
			finished = false;
		} else {
			tasks.add(new MultiSubdividorThread(subdividor, this));
		}
		return this;
	}

	public HE_MeshCollection subdivideThreaded(final HES_Subdividor subdividor) {
		tasks.add(new MultiSubdividorThread(subdividor, this));
		return this;
	}

	public HE_MeshCollection simplify(final HES_Simplifier simplifier) {
		if (finished) {
			for (HE_Mesh mesh : meshes) {
				mesh.simplify(simplifier);
				mesh.clearPrecomputed();
			}
			finished = false;
		} else {
			tasks.add(new MultiSimplifierThread(simplifier, this));
		}

		return this;
	}

	public HE_MeshCollection simplifyThreaded(final HES_Simplifier simplifier) {
		tasks.add(new MultiSimplifierThread(simplifier, this));
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_MeshIterator mItr() {
		return new HE_MeshIterator(this);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public HE_Mesh getMesh(final int i) {
		return meshes.get(i);
	}

	public List<HE_Mesh> toList() {
		return meshes;
	}

	/**
	 *
	 *
	 * @param i
	 * @param mesh
	 * @return
	 */
	public HE_Mesh set(final int i, final HE_Mesh mesh) {
		return meshes.set(i, mesh);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public boolean add(final HE_Mesh mesh) {
		return meshes.add(mesh);
	}

	public boolean addAll(final Collection<? extends HE_Mesh> meshes) {
		return this.meshes.addAll(meshes);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public boolean remove(final HE_Mesh mesh) {
		return meshes.remove(mesh);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfMeshes() {
		return meshes.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	public int size() {
		return meshes.size();
	}

	public void update() {
		if (future == null) {
			if (tasks.size() > 0) {
				if (executor == null) {
					executor = Executors.newFixedThreadPool(1);
				}
				future = executor.submit(tasks.removeFirst());
				finished = false;
			} else {
				if (executor != null) {
					executor.shutdown();
				}
				executor = null;
				finished = true;
			}
		} else if (future.isDone()) {
			future = null;
			finished = true;

		} else if (future.isCancelled()) {
			future = null;
			finished = true;

		}
	}

	/**
	 *
	 */
	class MultiCreatorThread implements Callable<HE_MeshCollection> {
		HEMC_MultiCreator creator;
		HE_MeshCollection collection;

		MultiCreatorThread(final HEMC_MultiCreator creator, final HE_MeshCollection collection) {
			this.creator = creator;
			this.collection = collection;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_MeshCollection call() {
			creator.create(collection);
			return collection;
		}
	}

	/**
	 *
	 */
	class MultiModifierThread implements Callable<HE_MeshCollection> {
		HEM_Modifier modifier;
		HE_MeshCollection collection;

		MultiModifierThread(final HEM_Modifier modifier, final HE_MeshCollection collection) {
			this.modifier = modifier;
			this.collection = collection;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_MeshCollection call() {
			for (int i = 0; i < meshes.size(); i++) {
				try {
					System.out.println(
							modifier.getClass().getSimpleName() + ": mesh " + i + " of " + meshes.size() + ".");
					HE_Mesh result = modifier.applySelf(meshes.get(i).get());
					meshes.set(i, result);
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
			return collection;
		}
	}

	/**
	 *
	 */
	class MultiSubdividorThread implements Callable<HE_MeshCollection> {
		HES_Subdividor subdividor;
		HE_MeshCollection collection;

		MultiSubdividorThread(final HES_Subdividor subdividor, final HE_MeshCollection collection) {
			this.subdividor = subdividor;
			this.collection = collection;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_MeshCollection call() {

			for (int i = 0; i < meshes.size(); i++) {
				try {
					System.out.println(
							subdividor.getClass().getSimpleName() + ": mesh " + i + " of " + meshes.size() + ".");
					HE_Mesh result = subdividor.applySelf(meshes.get(i).get());
					meshes.set(i, result);
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
			return collection;
		}
	}

	/**
	 *
	 */
	class MultiSimplifierThread implements Callable<HE_MeshCollection> {
		HES_Simplifier simplifier;
		HE_MeshCollection collection;

		MultiSimplifierThread(final HES_Simplifier simplifier, final HE_MeshCollection collection) {
			this.simplifier = simplifier;
			this.collection = collection;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_MeshCollection call() {
			for (int i = 0; i < meshes.size(); i++) {
				try {
					System.out.println(
							simplifier.getClass().getSimpleName() + ": mesh " + i + " of " + meshes.size() + ".");
					HE_Mesh result = simplifier.applySelf(meshes.get(i).get());
					meshes.set(i, result);
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
			return collection;
		}
	}

	public boolean isFinished() {
		return finished;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<HE_Mesh> iterator() {

		return meshes.iterator();
	}

}
