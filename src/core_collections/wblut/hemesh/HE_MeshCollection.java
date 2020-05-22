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

/**
 *
 */
public class HE_MeshCollection implements Iterable<HE_Mesh> {
	/**  */
	final List<HE_Mesh> meshes;
	/**  */
	Future<HE_MeshCollection> future;
	/**  */
	ExecutorService executor;
	/**  */
	LinkedList<Callable<HE_MeshCollection>> tasks;
	/**  */
	boolean finished;

	/**
	 *
	 */
	public HE_MeshCollection() {
		super();
		meshes = new FastList<>();
		tasks = new LinkedList<>();
		future = null;
		executor = null;
		finished = true;
	}

	/**
	 *
	 *
	 * @param creator
	 */
	public HE_MeshCollection(final HEMC_MultiCreator creator) {
		this();
		meshes.addAll(creator.create().meshes);
	}

	/**
	 *
	 *
	 * @param creator
	 */
	public void createThreaded(final HEMC_MultiCreator creator) {
		if (finished) {
			executor = Executors.newFixedThreadPool(1);
			future = executor.submit(new MultiCreatorThread(creator, this));
			tasks = new LinkedList<>();
			finished = false;
		}
	}

	/**
	 *
	 *
	 * @param modifier
	 * @return
	 */
	public HE_MeshCollection modify(final HEM_Modifier modifier) {
		if (finished) {
			for (final HE_Mesh mesh : meshes) {
				modifier.apply(mesh);
				mesh.clearPrecomputed();
			}
		} else {
			tasks.add(new MultiModifierThread(modifier, this));
		}
		return this;
	}

	/**
	 *
	 *
	 * @param modifier
	 * @return
	 */
	public HE_MeshCollection modifyThreaded(final HEM_Modifier modifier) {
		tasks.add(new MultiModifierThread(modifier, this));
		return this;
	}

	public HE_MeshCollection modifyThreaded(final HEM_Modifier modifier, final int start, final int end) {
		tasks.add(new MultiModifierThread(modifier, this, start, end));
		return this;
	}

	/**
	 *
	 *
	 * @param subdividor
	 * @return
	 */
	public HE_MeshCollection subdivide(final HES_Subdividor subdividor) {
		if (finished) {
			for (final HE_Mesh mesh : meshes) {
				subdividor.apply(mesh);
				mesh.clearPrecomputed();
			}
		} else {
			tasks.add(new MultiSubdividorThread(subdividor, this));
		}
		return this;
	}

	/**
	 *
	 *
	 * @param subdividor
	 * @return
	 */
	public HE_MeshCollection subdivideThreaded(final HES_Subdividor subdividor) {
		tasks.add(new MultiSubdividorThread(subdividor, this));
		return this;
	}

	public HE_MeshCollection subdivideThreaded(final HES_Subdividor subdividor, final int start, final int end) {
		tasks.add(new MultiSubdividorThread(subdividor, this, start, end));
		return this;
	}

	/**
	 *
	 *
	 * @param simplifier
	 * @return
	 */
	public HE_MeshCollection simplify(final HES_Simplifier simplifier) {
		if (finished) {
			for (final HE_Mesh mesh : meshes) {
				mesh.simplify(simplifier);
				mesh.clearPrecomputed();
			}
			finished = false;
		} else {
			tasks.add(new MultiSimplifierThread(simplifier, this));
		}
		return this;
	}

	/**
	 *
	 *
	 * @param simplifier
	 * @return
	 */
	public HE_MeshCollection simplifyThreaded(final HES_Simplifier simplifier) {
		tasks.add(new MultiSimplifierThread(simplifier, this));
		return this;
	}

	public HE_MeshCollection simplifyThreaded(final HES_Simplifier simplifier, final int start, final int end) {
		tasks.add(new MultiSimplifierThread(simplifier, this, start, end));
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

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public HE_Mesh get(final int i) {
		return meshes.get(i);
	}

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @param meshes
	 * @return
	 */
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

	/**
	 *
	 */
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
		/**  */
		HEMC_MultiCreator creator;
		/**  */
		HE_MeshCollection collection;

		/**
		 *
		 *
		 * @param creator
		 * @param collection
		 */
		MultiCreatorThread(final HEMC_MultiCreator creator, final HE_MeshCollection collection) {
			this.creator = creator;
			this.collection = collection;
		}

		/**
		 *
		 *
		 * @return
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
		/**  */
		HEM_Modifier modifier;
		/**  */
		HE_MeshCollection collection;
		int start, end;

		/**
		 *
		 *
		 * @param modifier
		 * @param collection
		 */
		MultiModifierThread(final HEM_Modifier modifier, final HE_MeshCollection collection) {
			this.modifier = modifier;
			this.collection = collection;
			start = 0;
			end = collection.size();
		}

		MultiModifierThread(final HEM_Modifier modifier, final HE_MeshCollection collection, final int start,
				final int end) {
			this.modifier = modifier;
			this.collection = collection;
			this.start = start;
			this.end = end;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public HE_MeshCollection call() {
			for (int i = start; i < ((end == -1) ? meshes.size() : end); i++) {
				try {
					System.out.println(
							modifier.getClass().getSimpleName() + ": mesh " + i + " of " + meshes.size() + ".");
					final HE_Mesh result = modifier.applySelf(meshes.get(i).get());
					meshes.set(i, result);
				} catch (final Exception e) {
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
		/**  */
		HES_Subdividor subdividor;
		/**  */
		HE_MeshCollection collection;
		int start, end;

		/**
		 *
		 *
		 * @param subdividor
		 * @param collection
		 */
		MultiSubdividorThread(final HES_Subdividor subdividor, final HE_MeshCollection collection) {
			this.subdividor = subdividor;
			this.collection = collection;
			start = 0;
			end = -1;
		}

		MultiSubdividorThread(final HES_Subdividor subdividor, final HE_MeshCollection collection, final int start,
				final int end) {
			this.subdividor = subdividor;
			this.collection = collection;
			this.start = start;
			this.end = end;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public HE_MeshCollection call() {
			for (int i = start; i < ((end == -1) ? meshes.size() : end); i++) {
				try {
					System.out.println(
							subdividor.getClass().getSimpleName() + ": mesh " + i + " of " + meshes.size() + ".");
					final HE_Mesh result = subdividor.applySelf(meshes.get(i).get());
					meshes.set(i, result);
				} catch (final Exception e) {
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
		/**  */
		HES_Simplifier simplifier;
		/**  */
		HE_MeshCollection collection;
		int start, end;

		/**
		 *
		 *
		 * @param simplifier
		 * @param collection
		 */
		MultiSimplifierThread(final HES_Simplifier simplifier, final HE_MeshCollection collection) {
			this.simplifier = simplifier;
			this.collection = collection;
			start = 0;
			end = -1;
		}

		MultiSimplifierThread(final HES_Simplifier simplifier, final HE_MeshCollection collection, final int start,
				final int end) {
			this.simplifier = simplifier;
			this.collection = collection;
			this.start = start;
			this.end = end;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public HE_MeshCollection call() {
			for (int i = start; i < ((end == -1) ? meshes.size() : end); i++) {
				try {
					System.out.println(
							simplifier.getClass().getSimpleName() + ": mesh " + i + " of " + meshes.size() + ".");
					final HE_Mesh result = simplifier.applySelf(meshes.get(i).get());
					meshes.set(i, result);
				} catch (final Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
			return collection;
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public Iterator<HE_Mesh> iterator() {
		return meshes.iterator();
	}
}
