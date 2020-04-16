package wblut.hemesh;

import java.util.Arrays;

public class HEM_MultiContours extends HEM_Modifier {
	public HEM_MultiContours setAttribute(final String name) {
		parameters.set("attribute", name);
		return this;
	}

	String getAttribute() {
		return (String) parameters.get("attribute", null);
	}

	public HEM_MultiContours setLevels(final double... levels) {
		Arrays.sort(levels);
		parameters.set("levels", levels);
		return this;
	}

	public HEM_MultiContours setLevelRange(final double min, final double max, final double increment) {
		final double[] levels = new double[(int) ((max - min) / increment) + 1];
		for (int i = 0; i <= (int) ((max - min) / increment); i++) {
			levels[i] = min + i * increment;
		}
		parameters.set("levels", levels);
		return this;
	}

	double[] getLevels() {
		return (double[]) parameters.get("levels", null);
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HE_Selection splitEdges = mesh.getNewSelection();
		final HE_Selection splitFaces = mesh.getNewSelection();
		final HE_Selection splitVertices = mesh.getNewSelection();
		final double[] levels = getLevels();
		int id = 0;
		final int numLevels = levels.length;
		for (final double level : levels) {
			final HEM_Contours mc = new HEM_Contours().setAttribute(getAttribute()).setLevel(level);
			if (id == 0) {
				mesh.modify(mc);
			} else {
				mesh.getSelection("inside").modify(mc);
			}
			splitEdges.addEdges(mesh.getSelection("splitEdges").getEdges());
			splitFaces.addFaces(mesh.getSelection("splitFaces").getFaces());
			splitVertices.addVertices(mesh.getSelection("splitVertices").getVertices());
			if (id == 0) {
				mesh.getNewSelection("band_" + id).addFaces(mesh.getSelection("outside").getFaces());
			}
			mesh.getNewSelection("inside_" + id).addFaces(mesh.getSelection("inside").getFaces());
			id++;
		}
		mesh.removeSelection("inside");
		mesh.removeSelection("outside");
		for (int i = 0; i < numLevels; i++) {
			mesh.renameSelection("inside_" + i, "band_" + (i + 1));
			mesh.getSelection("band_" + (i + 1)).removeFaces(mesh.getSelection("inside_" + (i + 1)).getFaces());
		}
		mesh.getSelection("splitEdges").clear();
		mesh.getSelection("splitFaces").clear();
		mesh.getSelection("splitVertices").clear();
		mesh.getSelection("splitEdges").addEdges(splitEdges.getEdges());
		mesh.getSelection("splitFaces").addFaces(splitFaces.getFaces());
		mesh.getSelection("splitVertices").addVertices(splitVertices.getVertices());
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HE_Mesh mesh = selection.getParent();
		final HE_Selection splitEdges = mesh.getNewSelection();
		final HE_Selection splitFaces = mesh.getNewSelection();
		final HE_Selection splitVertices = mesh.getNewSelection();
		final double[] levels = getLevels();
		int id = 0;
		final int numLevels = levels.length;
		for (final double level : levels) {
			final HEM_Contours mc = new HEM_Contours().setAttribute(getAttribute()).setLevel(level);
			if (id == 0) {
				selection.modify(mc);
			} else {
				mesh.getSelection("inside").modify(mc);
			}
			splitEdges.addEdges(mesh.getSelection("splitEdges").getEdges());
			splitFaces.addFaces(mesh.getSelection("splitFaces").getFaces());
			splitVertices.addVertices(mesh.getSelection("splitVertices").getVertices());
			if (id == 0) {
				mesh.getNewSelection("band_" + id).addFaces(mesh.getSelection("outside").getFaces());
			}
			mesh.getNewSelection("inside_" + id).addFaces(mesh.getSelection("inside").getFaces());
			id++;
		}
		mesh.removeSelection("inside");
		mesh.removeSelection("outside");
		for (int i = 0; i < numLevels; i++) {
			mesh.renameSelection("inside_" + i, "band_" + (i + 1));
			mesh.getSelection("band_" + (i + 1)).removeFaces(mesh.getSelection("inside_" + (i + 1)).getFaces());
		}
		mesh.getSelection("splitEdges").clear();
		mesh.getSelection("splitFaces").clear();
		mesh.getSelection("splitVertices").clear();
		mesh.getSelection("splitEdges").addEdges(splitEdges.getEdges());
		mesh.getSelection("splitFaces").addFaces(splitFaces.getFaces());
		mesh.getSelection("splitVertices").addVertices(splitVertices.getVertices());
		return mesh;
	}
}
