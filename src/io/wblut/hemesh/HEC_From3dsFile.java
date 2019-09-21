/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import wblut.geom.WB_Point;

/**
 *
 */
public class HEC_From3dsFile extends HEC_Creator {

	/**
	 * 
	 */
	Scene3ds _scene;

	/**
	 * 
	 */
	private String path;

	/**
	 * 
	 */
	private double scale;

	/**
	 * 
	 */
	public HEC_From3dsFile() {
		super();
		scale = 1;
		path = null;
		setOverride(true);
	}

	/**
	 * 
	 *
	 * @param path
	 */
	public HEC_From3dsFile(final String path) {
		super();
		this.path = path;
		scale = 1;
		setOverride(true);
	}

	/**
	 * 
	 *
	 * @param path
	 * @return
	 */
	public HEC_From3dsFile setPath(final String path) {
		this.path = path;
		return this;
	}

	/**
	 * 
	 *
	 * @param f
	 * @return
	 */
	@Override
	public HEC_From3dsFile setScale(final double f) {
		scale = f;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (path == null) {
			return new HE_Mesh();
		}
		try {
			loadScene(path);
		} catch (final Exception3ds e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final HE_Mesh mesh = new HE_Mesh();
		for (final Mesh3ds mesh3d : _scene.mMesh) {
			final HEC_FromFacelist creator = new HEC_FromFacelist();
			creator.setVertices(mesh3d.mVertex);
			creator.setFaces(mesh3d.mFace);
			creator.setCheckDuplicateVertices(true);
			mesh.add(new HE_Mesh(creator));
		}
		return mesh;
	}

	/**
	 * 
	 */
	class Mesh3ds {

		/**
		 * 
		 */
		String mName = "";

		/**
		 * 
		 */
		WB_Point[] mVertex = new WB_Point[0];

		/**
		 * 
		 */
		int[][] mFace = new int[0][3];
	}

	/**
	 * 
	 */
	class TextDecode3ds {

		/**
		 * 
		 */
		StringBuffer mText = new StringBuffer(1024 * 4);

		/**
		 * 
		 */
		public void clear() {
			mText.setLength(0);
		}

		/**
		 * 
		 *
		 * @return
		 */
		public String text() {
			return mText.toString();
		}
	}

	/**
	 * 
	 */
	class Decode3ds {

		/**
		 * 
		 */
		private TextDecode3ds mDecCont = null;

		/**
		 * 
		 */
		private int mDecLevel = 0;

		/**
		 * 
		 */
		private int mLevel = 0;

		/**
		 * 
		 */
		private String mNL = "\n";

		/**
		 * 
		 *
		 * @param decode
		 * @param level
		 */
		public Decode3ds(final TextDecode3ds decode, final int level) {
			mDecCont = decode;
			mDecLevel = level;
			mDecCont.clear();
			mNL = System.getProperty("line.separator");
		}

		/**
		 * 
		 */
		public void enter() {
			mLevel++;
		}

		/**
		 * 
		 */
		public void leave() {
			mLevel--;
		}

		/**
		 * 
		 *
		 * @param str
		 */
		public void println(final String str) {
			for (int i = 0; i < mLevel; i++) {
				mDecCont.mText.append("  ");
			}
			mDecCont.mText.append(str + mNL);
		}

		/**
		 * 
		 *
		 * @param buf
		 * @param offset
		 * @param n
		 */
		public void printBytes(final byte[] buf, int offset, int n) {
			if (mDecLevel >= Scene3ds.DECODE_ALL) {
				while (n > 0) {
					for (int i = 0; i < mLevel; i++) {
						mDecCont.mText.append("  ");
					}
					int run = n;
					if (run > 20) {
						run = 20;
					}
					for (int i = 0; i < run; i++) {
						mDecCont.mText.append(intToHex(buf[offset], 2) + " ");
						offset++;
					}
					n -= run;
					mDecCont.mText.append(mNL);
				}
			}
		}

		/**
		 * 
		 *
		 * @param val
		 * @param digits
		 * @return
		 */
		public String intToHex(final int val, final int digits) {
			final char[] buf = new char[8];
			final char[] lut = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			int shift = (digits - 1) * 4;
			for (int i = 0; i < digits; i++) {
				buf[i] = lut[val >> shift & 0xf];
				shift -= 4;
			}
			return new String(buf, 0, digits);
		}

		/**
		 * 
		 *
		 * @param id
		 * @param length
		 */
		public void printHead(final int id, final int length) {
			String name = "UNKNOWN";
			boolean known = false;
			for (int i = 0; i < mChunkInfo.length; i++) {
				if (mChunkInfo[i].id == id) {
					name = mChunkInfo[i].name;
					known = mChunkInfo[i].known;
					break;
				}
			}
			if (known == true || mDecLevel >= Scene3ds.DECODE_USED_PARAMS) {
				println(name + "  id=0x" + intToHex(id, 4) + " length=" + length);
			}
		}

		/**
		 * 
		 */
		private class ChunkInfo {

			/**
			 * 
			 */
			boolean known;

			/**
			 * 
			 */
			int id;

			/**
			 * 
			 */
			String name;

			/**
			 * 
			 *
			 * @param known
			 * @param id
			 * @param name
			 */
			ChunkInfo(final boolean known, final int id, final String name) {
				this.known = known;
				this.id = id;
				this.name = name;
			}
		}

		/**
		 * 
		 */
		private static final int CHUNK_M3DMAGIC = 0x4D4D, CHUNK_MDATA = 0x3D3D, CHUNK_MAT_ENTRY = 0xAFFF,
				CHUNK_MAT_NAME = 0xA000, CHUNK_NAMED_OBJECT = 0x4000, CHUNK_N_TRI_OBJECT = 0x4100,
				CHUNK_POINT_ARRAY = 0x4110, CHUNK_TEX_VERTS = 0x4140, CHUNK_MESH_TEXTURE_INFO = 0x4170,
				CHUNK_MESH_MATRIX = 0x4160,
				// CHUNK_MESH_COLOR = 0x4165,
				CHUNK_FACE_ARRAY = 0x4120, CHUNK_MSH_MAT_GROUP = 0x4130, CHUNK_SMOOTH_GROUP = 0x4150,
				CHUNK_N_CAMERA = 0x4700,
				// CHUNK_CAM_SEE_CONE = 0x4710,
				CHUNK_CAM_RANGES = 0x4720, CHUNK_KFDATA = 0xB000, CHUNK_KFSEG = 0xB008, CHUNK_OBJECT_NODE_TAG = 0xB002,
				CHUNK_NODE_ID = 0xB030, CHUNK_NODE_HDR = 0xB010, CHUNK_PIVOT = 0xB013, CHUNK_POS_TRACK_TAG = 0xB020,
				CHUNK_ROT_TRACK_TAG = 0xB021, CHUNK_SCL_TRACK_TAG = 0xB022, CHUNK_MORPH_TRACK_TAG = 0xB026,
				CHUNK_HIDE_TRACK_TAG = 0xB029, CHUNK_TARGET_NODE_TAG = 0xB004, CHUNK_CAMERA_NODE_TAG = 0xB003,
				CHUNK_FOV_TRACK_TAG = 0xB023, CHUNK_ROLL_TRACK_TAG = 0xB024;
		// CHUNK_AMBIENT_NODE_TAG = 0xB001;
		/**
		 * 
		 */
		private final ChunkInfo[] mChunkInfo = {
				// 3DS File Chunk IDs
				new ChunkInfo(true, CHUNK_M3DMAGIC, "M3DMAGIC"), new ChunkInfo(false, 0x2D2D, "SMAGIC"),
				new ChunkInfo(false, 0x2D3D, "LMAGIC"), new ChunkInfo(false, 0x3DAA, "MLIBMAGIC"),
				new ChunkInfo(false, 0x3DFF, "MATMAGIC"), new ChunkInfo(false, 0x0002, "M3D_VERSION"),
				new ChunkInfo(false, 0x0005, "M3D_KFVERSION"),
				// Mesh Chunk Ids
				new ChunkInfo(true, CHUNK_MDATA, "MDATA"), new ChunkInfo(false, 0x3D3E, "MESH_VERSION"),
				new ChunkInfo(false, 0x0010, "COLOR_F"), new ChunkInfo(false, 0x0011, "COLOR_24"),
				new ChunkInfo(false, 0x0012, "LIN_COLOR_24"), new ChunkInfo(false, 0x0013, "LIN_COLOR_F"),
				new ChunkInfo(false, 0x0030, "INT_PERCENTAGE"), new ChunkInfo(false, 0x0031, "FLOAT_PERCENTAGE"),
				new ChunkInfo(false, 0x0100, "MASTER_SCALE"), new ChunkInfo(false, 0x1100, "BIT_MAP"),
				new ChunkInfo(false, 0x1101, "USE_BIT_MAP"), new ChunkInfo(false, 0x1200, "SOLID_BGND"),
				new ChunkInfo(false, 0x1201, "USE_SOLID_BGND"), new ChunkInfo(false, 0x1300, "V_GRADIENT"),
				new ChunkInfo(false, 0x1301, "USE_V_GRADIENT"), new ChunkInfo(false, 0x1400, "LO_SHADOW_BIAS"),
				new ChunkInfo(false, 0x1410, "HI_SHADOW_BIAS"), new ChunkInfo(false, 0x1420, "SHADOW_MAP_SIZE"),
				new ChunkInfo(false, 0x1430, "SHADOW_SAMPLES"), new ChunkInfo(false, 0x1440, "SHADOW_RANGE"),
				new ChunkInfo(false, 0x1450, "SHADOW_FILTER"), new ChunkInfo(false, 0x1460, "RAY_BIAS"),
				new ChunkInfo(false, 0x1500, "O_CONSTS"), new ChunkInfo(false, 0x2100, "AMBIENT_LIGHT"),
				new ChunkInfo(false, 0x2200, "FOG"), new ChunkInfo(false, 0x2201, "USE_FOG"),
				new ChunkInfo(false, 0x2210, "FOG_BGND"), new ChunkInfo(false, 0x2300, "DISTANCE_CUE"),
				new ChunkInfo(false, 0x2301, "USE_DISTANCE_CUE"), new ChunkInfo(false, 0x2302, "LAYER_FOG"),
				new ChunkInfo(false, 0x2303, "USE_LAYER_FOG"), new ChunkInfo(false, 0x2310, "DCUE_BGND"),
				new ChunkInfo(false, 0x3000, "DEFAULT_VIEW"), new ChunkInfo(false, 0x3010, "VIEW_TOP"),
				new ChunkInfo(false, 0x3020, "VIEW_BOTTOM"), new ChunkInfo(false, 0x3030, "VIEW_LEFT"),
				new ChunkInfo(false, 0x3040, "VIEW_RIGHT"), new ChunkInfo(false, 0x3050, "VIEW_FRONT"),
				new ChunkInfo(false, 0x3060, "VIEW_BACK"), new ChunkInfo(false, 0x3070, "VIEW_USER"),
				new ChunkInfo(false, 0x3080, "VIEW_CAMERA"), new ChunkInfo(false, 0x3090, "VIEW_WINDOW"),
				new ChunkInfo(true, CHUNK_NAMED_OBJECT, "NAMED_OBJECT"), new ChunkInfo(false, 0x4010, "OBJ_HIDDEN"),
				new ChunkInfo(false, 0x4011, "OBJ_VIS_LOFTER"), new ChunkInfo(false, 0x4012, "OBJ_DOESNT_CAST"),
				new ChunkInfo(false, 0x4013, "OBJ_MATTE"), new ChunkInfo(false, 0x4014, "OBJ_FAST"),
				new ChunkInfo(false, 0x4015, "OBJ_PROCEDURAL"), new ChunkInfo(false, 0x4016, "OBJ_FROZEN"),
				new ChunkInfo(false, 0x4017, "OBJ_DONT_RCVSHADOW"),
				new ChunkInfo(true, CHUNK_N_TRI_OBJECT, "N_TRI_OBJECT"),
				new ChunkInfo(true, CHUNK_POINT_ARRAY, "POINT_ARRAY"), new ChunkInfo(false, 0x4111, "POINT_FLAG_ARRAY"),
				new ChunkInfo(true, CHUNK_FACE_ARRAY, "FACE_ARRAY"),
				new ChunkInfo(true, CHUNK_MSH_MAT_GROUP, "MSH_MAT_GROUP"),
				new ChunkInfo(false, 0x4131, "OLD_MAT_GROUP"), new ChunkInfo(true, CHUNK_TEX_VERTS, "TEX_VERTS"),
				new ChunkInfo(true, CHUNK_SMOOTH_GROUP, "SMOOTH_GROUP"),
				new ChunkInfo(true, CHUNK_MESH_MATRIX, "MESH_MATRIX"), new ChunkInfo(false, 0x4165, "MESH_COLOR"),
				// new ChunkInfo(true, CHUNK_MESH_COLOR, "MESH_COLOR" ),
				new ChunkInfo(true, CHUNK_MESH_TEXTURE_INFO, "MESH_TEXTURE_INFO"),
				new ChunkInfo(false, 0x4181, "PROC_NAME"), new ChunkInfo(false, 0x4182, "PROC_DATA"),
				new ChunkInfo(false, 0x4190, "MSH_BOXMAP"), new ChunkInfo(false, 0x4400, "N_D_L_OLD"),
				new ChunkInfo(false, 0x4500, "N_CAM_OLD"), new ChunkInfo(false, 0x4600, "N_DIRECT_LIGHT"),
				new ChunkInfo(false, 0x4610, "DL_SPOTLIGHT"), new ChunkInfo(false, 0x4620, "DL_OFF"),
				new ChunkInfo(false, 0x4625, "DL_ATTENUATE"), new ChunkInfo(false, 0x4627, "DL_RAYSHAD"),
				new ChunkInfo(false, 0x4630, "DL_SHADOWED"), new ChunkInfo(false, 0x4640, "DL_LOCAL_SHADOW"),
				new ChunkInfo(false, 0x4641, "DL_LOCAL_SHADOW2"), new ChunkInfo(false, 0x4650, "DL_SEE_CONE"),
				new ChunkInfo(false, 0x4651, "DL_SPOT_RECTANGULAR"), new ChunkInfo(false, 0x4652, "DL_SPOT_OVERSHOOT"),
				new ChunkInfo(false, 0x4653, "DL_SPOT_PROJECTOR"), new ChunkInfo(false, 0x4654, "DL_EXCLUDE"),
				new ChunkInfo(false, 0x4655, "DL_RANGE"), new ChunkInfo(false, 0x4656, "DL_SPOT_ROLL"),
				new ChunkInfo(false, 0x4657, "DL_SPOT_ASPECT"), new ChunkInfo(false, 0x4658, "DL_RAY_BIAS"),
				new ChunkInfo(false, 0x4659, "DL_INNER_RANGE"), new ChunkInfo(false, 0x465A, "DL_OUTER_RANGE"),
				new ChunkInfo(false, 0x465B, "DL_MULTIPLIER"), new ChunkInfo(false, 0x4680, "N_AMBIENT_LIGHT"),
				new ChunkInfo(true, CHUNK_N_CAMERA, "N_CAMERA"), new ChunkInfo(false, 0x4710, "CAM_SEE_CONE"),
				// new ChunkInfo(true, CHUNK_CAM_SEE_CONE, "CAM_SEE_CONE" ),
				new ChunkInfo(true, CHUNK_CAM_RANGES, "CAM_RANGES"), new ChunkInfo(false, 0x4F00, "HIERARCHY"),
				new ChunkInfo(false, 0x4F10, "PARENT_OBJECT"), new ChunkInfo(false, 0x4F20, "PIVOT_OBJECT"),
				new ChunkInfo(false, 0x4F30, "PIVOT_LIMITS"), new ChunkInfo(false, 0x4F40, "PIVOT_ORDER"),
				new ChunkInfo(false, 0x4F50, "XLATE_RANGE"), new ChunkInfo(false, 0x5000, "POLY_2D"),
				// Flags in shaper file that tell whether polys make up an ok
				// shape
				new ChunkInfo(false, 0x5010, "SHAPE_OK"), new ChunkInfo(false, 0x5011, "SHAPE_NOT_OK"),
				new ChunkInfo(false, 0x5020, "SHAPE_HOOK"), new ChunkInfo(false, 0x6000, "PATH_3D"),
				new ChunkInfo(false, 0x6005, "PATH_MATRIX"), new ChunkInfo(false, 0x6010, "SHAPE_2D"),
				new ChunkInfo(false, 0x6020, "M_SCALE"), new ChunkInfo(false, 0x6030, "M_TWIST"),
				new ChunkInfo(false, 0x6040, "M_TEETER"), new ChunkInfo(false, 0x6050, "M_FIT"),
				new ChunkInfo(false, 0x6060, "M_BEVEL"), new ChunkInfo(false, 0x6070, "XZ_CURVE"),
				new ChunkInfo(false, 0x6080, "YZ_CURVE"), new ChunkInfo(false, 0x6090, "INTERPCT"),
				new ChunkInfo(false, 0x60A0, "DEFORM_LIMIT"),
				// Flags for Modeler options
				new ChunkInfo(false, 0x6100, "USE_CONTOUR"), new ChunkInfo(false, 0x6110, "USE_TWEEN"),
				new ChunkInfo(false, 0x6120, "USE_SCALE"), new ChunkInfo(false, 0x6130, "USE_TWIST"),
				new ChunkInfo(false, 0x6140, "USE_TEETER"), new ChunkInfo(false, 0x6150, "USE_FIT"),
				new ChunkInfo(false, 0x6160, "USE_BEVEL"),
				// Viewport description chunks
				new ChunkInfo(false, 0x7000, "VIEWPORT_LAYOUT_OLD"), new ChunkInfo(false, 0x7010, "VIEWPORT_DATA_OLD"),
				new ChunkInfo(false, 0x7001, "VIEWPORT_LAYOUT"), new ChunkInfo(false, 0x7011, "VIEWPORT_DATA"),
				new ChunkInfo(false, 0x7012, "VIEWPORT_DATA_3"), new ChunkInfo(false, 0x7020, "VIEWPORT_SIZE"),
				new ChunkInfo(false, 0x7030, "NETWORK_VIEW"),
				// External Application Data
				new ChunkInfo(false, 0x8000, "XDATA_SECTION"), new ChunkInfo(false, 0x8001, "XDATA_ENTRY"),
				new ChunkInfo(false, 0x8002, "XDATA_APPNAME"), new ChunkInfo(false, 0x8003, "XDATA_STRING"),
				new ChunkInfo(false, 0x8004, "XDATA_FLOAT"), new ChunkInfo(false, 0x8005, "XDATA_DOUBLE"),
				new ChunkInfo(false, 0x8006, "XDATA_SHORT"), new ChunkInfo(false, 0x8007, "XDATA_LONG"),
				new ChunkInfo(false, 0x8008, "XDATA_VOID"), new ChunkInfo(false, 0x8009, "XDATA_GROUP"),
				new ChunkInfo(false, 0x800A, "XDATA_RFU6"), new ChunkInfo(false, 0x800B, "XDATA_RFU5"),
				new ChunkInfo(false, 0x800C, "XDATA_RFU4"), new ChunkInfo(false, 0x800D, "XDATA_RFU3"),
				new ChunkInfo(false, 0x800E, "XDATA_RFU2"), new ChunkInfo(false, 0x800F, "XDATA_RFU1"),
				new ChunkInfo(false, 0x80F0, "PARENT_NAME"),
				// Material Chunk IDs
				new ChunkInfo(true, CHUNK_MAT_ENTRY, "MAT_ENTRY"), new ChunkInfo(true, CHUNK_MAT_NAME, "MAT_NAME"),
				new ChunkInfo(false, 0xA010, "MAT_AMBIENT"), new ChunkInfo(false, 0xA020, "MAT_DIFFUSE"),
				new ChunkInfo(false, 0xA030, "MAT_SPECULAR"), new ChunkInfo(false, 0xA040, "MAT_SHININESS"),
				new ChunkInfo(false, 0xA041, "MAT_SHIN2PCT"), new ChunkInfo(false, 0xA042, "MAT_SHIN3PCT"),
				new ChunkInfo(false, 0xA050, "MAT_TRANSPARENCY"), new ChunkInfo(false, 0xA052, "MAT_XPFALL"),
				new ChunkInfo(false, 0xA053, "MAT_REFBLUR"), new ChunkInfo(false, 0xA080, "MAT_SELF_ILLUM"),
				new ChunkInfo(false, 0xA081, "MAT_TWO_SIDE"), new ChunkInfo(false, 0xA082, "MAT_DECAL"),
				new ChunkInfo(false, 0xA083, "MAT_ADDITIVE"), new ChunkInfo(false, 0xA084, "MAT_SELF_ILPCT"),
				new ChunkInfo(false, 0xA085, "MAT_WIRE"), new ChunkInfo(false, 0xA086, "MAT_SUPERSMP"),
				new ChunkInfo(false, 0xA087, "MAT_WIRESIZE"), new ChunkInfo(false, 0xA088, "MAT_FACEMAP"),
				new ChunkInfo(false, 0xA08A, "MAT_XPFALLIN"), new ChunkInfo(false, 0xA08C, "MAT_PHONGSOFT"),
				new ChunkInfo(false, 0xA08E, "MAT_WIREABS"), new ChunkInfo(false, 0xA100, "MAT_SHADING"),
				new ChunkInfo(false, 0xA200, "MAT_TEXMAP"), new ChunkInfo(false, 0xA210, "MAT_OPACMAP"),
				new ChunkInfo(false, 0xA220, "MAT_REFLMAP"), new ChunkInfo(false, 0xA230, "MAT_BUMPMAP"),
				new ChunkInfo(false, 0xA204, "MAT_SPECMAP"), new ChunkInfo(false, 0xA240, "MAT_USE_XPFALL"),
				new ChunkInfo(false, 0xA250, "MAT_USE_REFBLUR"), new ChunkInfo(false, 0xA252, "MAT_BUMP_PERCENT"),
				new ChunkInfo(false, 0xA300, "MAT_MAPNAME"), new ChunkInfo(false, 0xA310, "MAT_ACUBIC"),
				new ChunkInfo(false, 0xA320, "MAT_SXP_TEXT_DATA"), new ChunkInfo(false, 0xA321, "MAT_SXP_TEXT2_DATA"),
				new ChunkInfo(false, 0xA322, "MAT_SXP_OPAC_DATA"), new ChunkInfo(false, 0xA324, "MAT_SXP_BUMP_DATA"),
				new ChunkInfo(false, 0xA325, "MAT_SXP_SPEC_DATA"), new ChunkInfo(false, 0xA326, "MAT_SXP_SHIN_DATA"),
				new ChunkInfo(false, 0xA328, "MAT_SXP_SELFI_DATA"),
				new ChunkInfo(false, 0xA32A, "MAT_SXP_TEXT_MASKDATA"),
				new ChunkInfo(false, 0xA32C, "MAT_SXP_TEXT2_MASKDATA"),
				new ChunkInfo(false, 0xA32E, "MAT_SXP_OPAC_MASKDATA"),
				new ChunkInfo(false, 0xA330, "MAT_SXP_BUMP_MASKDATA"),
				new ChunkInfo(false, 0xA332, "MAT_SXP_SPEC_MASKDATA"),
				new ChunkInfo(false, 0xA334, "MAT_SXP_SHIN_MASKDATA"),
				new ChunkInfo(false, 0xA336, "MAT_SXP_SELFI_MASKDATA"),
				new ChunkInfo(false, 0xA338, "MAT_SXP_REFL_MASKDATA"), new ChunkInfo(false, 0xA33A, "MAT_TEX2MAP"),
				new ChunkInfo(false, 0xA33C, "MAT_SHINMAP"), new ChunkInfo(false, 0xA33D, "MAT_SELFIMAP"),
				new ChunkInfo(false, 0xA33E, "MAT_TEXMASK"), new ChunkInfo(false, 0xA340, "MAT_TEX2MASK"),
				new ChunkInfo(false, 0xA342, "MAT_OPACMASK"), new ChunkInfo(false, 0xA344, "MAT_BUMPMASK"),
				new ChunkInfo(false, 0xA346, "MAT_SHINMASK"), new ChunkInfo(false, 0xA348, "MAT_SPECMASK"),
				new ChunkInfo(false, 0xA34A, "MAT_SELFIMASK"), new ChunkInfo(false, 0xA34C, "MAT_REFLMASK"),
				new ChunkInfo(false, 0xA350, "MAT_MAP_TILINGOLD"), new ChunkInfo(false, 0xA351, "MAT_MAP_TILING"),
				new ChunkInfo(false, 0xA352, "MAT_MAP_TEXBLUR_OLD"), new ChunkInfo(false, 0xA353, "MAT_MAP_TEXBLUR"),
				new ChunkInfo(false, 0xA354, "MAT_MAP_USCALE"), new ChunkInfo(false, 0xA356, "MAT_MAP_VSCALE"),
				new ChunkInfo(false, 0xA358, "MAT_MAP_UOFFSET"), new ChunkInfo(false, 0xA35A, "MAT_MAP_VOFFSET"),
				new ChunkInfo(false, 0xA35C, "MAT_MAP_ANG"), new ChunkInfo(false, 0xA360, "MAT_MAP_COL1"),
				new ChunkInfo(false, 0xA362, "MAT_MAP_COL2"), new ChunkInfo(false, 0xA364, "MAT_MAP_RCOL"),
				new ChunkInfo(false, 0xA366, "MAT_MAP_GCOL"), new ChunkInfo(false, 0xA368, "MAT_MAP_BCOL"),
				// Keyframe Chunk IDs
				new ChunkInfo(true, CHUNK_KFDATA, "KFDATA"), new ChunkInfo(false, 0xB00A, "KFHDR"),
				new ChunkInfo(false, 0xB001, "AMBIENT_NODE_TAG"),
				// new ChunkInfo(true, CHUNK_AMBIENT_NODE_TAG,
				// "AMBIENT_NODE_TAG" ),
				new ChunkInfo(true, CHUNK_OBJECT_NODE_TAG, "OBJECT_NODE_TAG"),
				new ChunkInfo(true, CHUNK_CAMERA_NODE_TAG, "CAMERA_NODE_TAG"),
				new ChunkInfo(true, CHUNK_TARGET_NODE_TAG, "TARGET_NODE_TAG"),
				new ChunkInfo(false, 0xB005, "LIGHT_NODE_TAG"), new ChunkInfo(false, 0xB006, "L_TARGET_NODE_TAG"),
				new ChunkInfo(false, 0xB007, "SPOTLIGHT_NODE_TAG"), new ChunkInfo(true, CHUNK_KFSEG, "KFSEG"),
				new ChunkInfo(false, 0xB009, "KFCURTIME"), new ChunkInfo(true, CHUNK_NODE_HDR, "NODE_HDR"),
				new ChunkInfo(false, 0xB011, "INSTANCE_NAME"), new ChunkInfo(false, 0xB012, "PRESCALE"),
				new ChunkInfo(true, CHUNK_PIVOT, "PIVOT"), new ChunkInfo(false, 0xB014, "BOUNDBOX"),
				new ChunkInfo(false, 0xB015, "MORPH_SMOOTH"), new ChunkInfo(true, CHUNK_POS_TRACK_TAG, "POS_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_ROT_TRACK_TAG, "ROT_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_SCL_TRACK_TAG, "SCL_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_FOV_TRACK_TAG, "FOV_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_ROLL_TRACK_TAG, "ROLL_TRACK_TAG"),
				new ChunkInfo(false, 0xB025, "COL_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_MORPH_TRACK_TAG, "MORPH_TRACK_TAG"),
				new ChunkInfo(false, 0xB027, "HOT_TRACK_TAG"), new ChunkInfo(false, 0xB028, "FALL_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_HIDE_TRACK_TAG, "HIDE_TRACK_TAG"),
				new ChunkInfo(true, CHUNK_NODE_ID, "NODE_ID") };
	}

	/**
	 * 
	 */
	public class Exception3ds extends java.lang.Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 *
		 * @param s
		 */
		public Exception3ds(final String s) {
			super(s);
		}
	}

	/**
	 * 
	 */
	public class Scene3ds {

		/**
		 * 
		 */
		public static final int DECODE_ALL = 3;

		/**
		 * 
		 */
		public static final int DECODE_USED_PARAMS = 2;

		/**
		 * 
		 */
		public static final int DECODE_USED_PARAMS_AND_CHUNKS = 1;
		// Array of meshes
		/**
		 * 
		 */
		ArrayList<Mesh3ds> mMesh = new ArrayList<Mesh3ds>(5);

		// Add mesh at end of mesh array
		/**
		 * 
		 *
		 * @param m
		 */
		void addMesh(final Mesh3ds m) {
			mMesh.add(m);
		}

		/**
		 * 
		 *
		 * @return
		 */
		public int meshes() {
			return mMesh.size();
		}

		/**
		 * 
		 *
		 * @param i
		 * @return
		 */
		public Mesh3ds mesh(final int i) {
			return mMesh.get(i);
		}

		/**
		 * 
		 *
		 * @param file_image
		 * @param decode
		 * @param level
		 * @throws Exception3ds
		 */
		public Scene3ds(final byte[] file_image, final TextDecode3ds decode, final int level) throws Exception3ds {
			if (decode != null) {
				mDecode = new Decode3ds(decode, level);
			}
			mFileData = file_image;
			mFileLength = file_image.length;
			mFilePos = 0;
			try {
				read3DS();
			} catch (final Exception3ds e) {
				throw new Exception3ds("3DS-parser: " + e.getMessage());
			} finally {
				mFileData = null;
				mDecode = null;
			}
		}

		/**
		 * 
		 *
		 * @param file_image
		 * @throws Exception3ds
		 */
		public Scene3ds(final byte[] file_image) throws Exception3ds {
			this(file_image, null, 0);
		}

		/**
		 * 
		 *
		 * @param file
		 * @param decode
		 * @param level
		 * @throws Exception3ds
		 */
		public Scene3ds(final File file, final TextDecode3ds decode, final int level) throws Exception3ds {
			if (decode != null) {
				mDecode = new Decode3ds(decode, level);
			}
			try {
				final RandomAccessFile raf = new RandomAccessFile(file, "r");
				mFileData = new byte[(int) raf.length()];
				raf.readFully(mFileData);
				raf.close();
				mFileLength = mFileData.length;
				mFilePos = 0;
				read3DS();
			} catch (final IOException e) {
				throw new Exception3ds("I/O problems: " + e.getMessage());
			} catch (final Exception3ds e) {
				throw new Exception3ds("3DS-parser: " + e.getMessage());
			} finally {
				mFileData = null;
				mDecode = null;
			}
		}

		/**
		 * 
		 *
		 * @param file
		 * @throws Exception3ds
		 */
		public Scene3ds(final File file) throws Exception3ds {
			this(file, null, 0);
		}

		/**
		 * 
		 *
		 * @param stream
		 * @param decode
		 * @param level
		 * @throws Exception3ds
		 */
		public Scene3ds(final InputStream stream, final TextDecode3ds decode, final int level) throws Exception3ds {
			if (decode != null) {
				mDecode = new Decode3ds(decode, level);
			}
			try {
				int buf_size = 4096, stored = 0, b;
				byte[] buf = new byte[buf_size];
				while ((b = stream.read()) != -1) {
					buf[stored++] = (byte) b;
					if (stored >= buf_size) {
						final byte[] tmp = new byte[buf_size * 2];
						System.arraycopy(buf, 0, tmp, 0, buf_size);
						buf_size *= 2;
						buf = tmp;
					}
				}
				mFileData = buf;
				mFileLength = stored;
				mFilePos = 0;
				read3DS();
			} catch (final IOException e) {
				throw new Exception3ds("I/O problems: " + e.getMessage());
			} catch (final Exception3ds e) {
				throw new Exception3ds("3DS-parser: " + e.getMessage());
			} finally {
				mFileData = null;
				mDecode = null;
			}
		}

		/**
		 * 
		 *
		 * @param stream
		 * @throws Exception3ds
		 */
		public Scene3ds(final InputStream stream) throws Exception3ds {
			this(stream, null, 0);
		}

		/**
		 * 
		 */
		private byte[] mFileData = null;

		/**
		 * 
		 */
		private int mFileLength = 0;

		/**
		 * 
		 */
		private int mFilePos = 0;

		/**
		 * 
		 */
		private Decode3ds mDecode = null;

		/**
		 * 
		 */
		private class Head {

			/**
			 * 
			 */
			public int id;

			/**
			 * 
			 */
			public int length;

			/**
			 * 
			 *
			 * @param id
			 * @param length
			 */
			public Head(final int id, final int length) {
				this.id = id;
				this.length = length;
			}
		}

		/**
		 * 
		 *
		 * @return
		 */
		private int filePos() {
			return mFilePos;
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private byte readByte() throws Exception3ds {
			if (mFilePos >= mFileLength) {
				throw new Exception3ds("Read out of bounds! File is probably corrupt.");
			}
			return mFileData[mFilePos++];
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private int readUnsignedShort() throws Exception3ds {
			if (mFilePos + 2 > mFileLength) {
				throw new Exception3ds("Read out of bounds! File is probably corrupt.");
			}
			final int val = mFileData[mFilePos] & 0xff | (mFileData[mFilePos + 1] & 0xff) << 8;
			mFilePos += 2;
			return val;
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private int readInt() throws Exception3ds {
			if (mFilePos + 4 > mFileLength) {
				throw new Exception3ds("Read out of bounds! File is probably corrupt.");
			}
			final int val = mFileData[mFilePos] & 0xff | (mFileData[mFilePos + 1] & 0xff) << 8
					| (mFileData[mFilePos + 2] & 0xff) << 16 | mFileData[mFilePos + 3] << 24;
			mFilePos += 4;
			return val;
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private float readFloat() throws Exception3ds {
			if (mFilePos + 4 > mFileLength) {
				throw new Exception3ds("Read out of bounds! File is probably corrupt.");
			}
			final int val = mFileData[mFilePos] & 0xff | (mFileData[mFilePos + 1] & 0xff) << 8
					| (mFileData[mFilePos + 2] & 0xff) << 16 | mFileData[mFilePos + 3] << 24;
			mFilePos += 4;
			return Float.intBitsToFloat(val);
		}

		/**
		 * 
		 *
		 * @param n
		 * @return
		 * @throws Exception3ds
		 */
		private int skipBytes(final int n) throws Exception3ds {
			if (n < 0) {
				throw new Exception3ds("Negative chunk size! File is probably corrupt.");
			} else if (mFilePos + n > mFileData.length) {
				throw new Exception3ds("Read out of bounds! File is probably corrupt.");
			}
			if (mDecode != null) {
				mDecode.printBytes(mFileData, mFilePos, n);
			}
			mFilePos += n;
			return n;
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void skipChunk(final int chunk_len) throws Exception3ds {
			if (mDecode != null) {
				mDecode.enter();
			}
			skipBytes(chunk_len);
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private Head read_HEAD() throws Exception3ds {
			final int id = readUnsignedShort();
			final int length = readInt();
			if (mDecode != null) {
				mDecode.printHead(id, length);
			}
			return new Head(id, length);
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private String read_NAME() throws Exception3ds {
			return read_NAME(32);
		}

		/**
		 * 
		 *
		 * @param length
		 * @return
		 * @throws Exception3ds
		 */
		private String read_NAME(final int length) throws Exception3ds {
			final byte[] buf = new byte[length];
			boolean terminated = false;
			int n;
			String name;
			for (n = 0; n < length; n++) {
				if ((buf[n] = readByte()) == 0) {
					terminated = true;
					break;
				}
			}
			if (terminated == false) {
				throw new Exception3ds("Name not terminated! File is probably corrupt.");
			}
			name = new String(buf, 0, n);
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
			return name;
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read3DS() throws Exception3ds {
			final Head head = read_HEAD();
			if (head.id != CHUNK_M3DMAGIC) {
				throw new Exception3ds("Bad signature! This is not a 3D Studio R4 .3ds file.");
			}
			read_M3DMAGIC(head.length - 6);
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_M3DMAGIC(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_MDATA:
					read_MDATA(head.length - 6);
					break;
				case CHUNK_KFDATA:
					read_KFDATA(head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_MDATA(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_NAMED_OBJECT:
					read_NAMED_OBJECT(head.length - 6);
					break;
				case CHUNK_MAT_ENTRY:
					read_MAT_ENTRY(head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void readColor(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_COL_RGB:
					readRGBColor();
					break;
				case CHUNK_COL_TRU:
					readTrueColor();
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @return
		 * @throws Exception3ds
		 */
		private float readPercentage(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			float val = 0.0f;
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_PERCENTW:
					// lvColor = readRGBColor();
					final int trans = readUnsignedShort();
					val = (float) (trans / 100.0);
					// System.out.println( "short: " + trans + " float: " + val
					// );
					break;
				case CHUNK_PERCENTF:
					val = readFloat();
					// System.out.println( "just float: " + val );
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
			return val;
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_MAT_ENTRY(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_MAT_NAME:
					read_NAME();
					break;
				case CHUNK_MAT_AMBIENT:
					readColor(head.length - 6);
					break;
				case CHUNK_MAT_SPECULAR:
					readColor(head.length - 6);
					break;
				case CHUNK_MAT_DIFFUSE:
					readColor(head.length - 6);
					break;
				case CHUNK_MAT_MAPNAME:
					read_NAME();
					break;
				case CHUNK_MAT_MAP:
					read_MAT_ENTRY(head.length - 6);
					break;
				// case CHUNK_MAT_SHININESS:
				// mat._shininess = readFloat();
				// break;
				case CHUNK_MAT_TRANSPARENCY:
					readPercentage(head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_NAMED_OBJECT(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			final String name = read_NAME();
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_N_TRI_OBJECT:
					read_N_TRI_OBJECT(name, head.length - 6);
					break;
				case CHUNK_N_LIGHT:
					read_N_LIGHT(name, head.length - 6);
					break;
				case CHUNK_N_CAMERA:
					read_N_CAMERA(name, head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void readSpotChunk(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_LIT_RAYSHAD:
					// pLight.mRayShadows = (readUnsignedShort() > 0);
					break;
				case CHUNK_LIT_SHADOWED:
					// pLight.mShadowed = (readUnsignedShort() > 0);
					break;
				case CHUNK_LIT_LOCAL_SHADOW:
					readFloat();
					readFloat();
					readFloat();
					break;
				case CHUNK_LIT_LOCAL_SHADOW2:
					readFloat();
					readFloat();
					readFloat();
					break;
				case CHUNK_LIT_SEE_CONE:
					// pLight.mCone = (readUnsignedShort() > 0);
					break;
				case CHUNK_LIT_SPOT_RECTANGULAR:
					// pLight.mRectangular = (readUnsignedShort() > 0);
					break;
				case CHUNK_LIT_SPOT_OVERSHOOT:
					// pLight.mOvershoot = (readUnsignedShort() > 0);
					break;
				case CHUNK_LIT_SPOT_PROJECTOR:
					// pLight.mProjector = (readUnsignedShort() > 0);
					read_NAME(64);
					break;
				case CHUNK_LIT_SPOT_RANGE:
					readFloat();
					break;
				case CHUNK_LIT_SPOT_ROLL:
					readFloat();
					break;
				case CHUNK_LIT_SPOT_ASPECT:
					readFloat();
					break;
				case CHUNK_LIT_RAY_BIAS:
					readFloat();
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param name
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_N_LIGHT(final String name, final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			readFloat();
			readFloat();
			readFloat();
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_LIT_OFF:
					readUnsignedShort();
					break;
				case CHUNK_LIT_SPOT:
					readSpotChunk(head.length - 6);
					break;
				case CHUNK_COL_RGB:
				case CHUNK_COL_LINRGB:
					readRGBColor();
					break;
				case CHUNK_COL_TRU:
				case CHUNK_COL_LINTRU:
					readTrueColor();
					break;
				case CHUNK_LIT_ATTENUATE:
					readFloat();
					break;
				case CHUNK_LIT_INNER_RANGE:
					readFloat();
					break;
				case CHUNK_LIT_OUTER_RANGE:
					readFloat();
					break;
				case CHUNK_LIT_MULTIPLIER:
					readFloat();
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void readRGBColor() throws Exception3ds {
			readFloat();
			readFloat();
			readFloat();
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void readTrueColor() throws Exception3ds {
			readByte();
			readByte();
			readByte();
		}

		/**
		 * 
		 *
		 * @param name
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_N_CAMERA(final String name, final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_CAM_RANGES:
					read_CAM_RANGES();
					break;
				case CHUNK_CAM_SEE_CONE:
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read_CAM_RANGES() throws Exception3ds {
			readFloat();
			readFloat();
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param name
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_N_TRI_OBJECT(final String name, final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			final Mesh3ds mes = new Mesh3ds();
			mes.mName = name;
			addMesh(mes);
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_POINT_ARRAY:
					mes.mVertex = read_POINT_ARRAY();
					break;
				case CHUNK_TEX_VERTS:
					read_TEX_VERTS();
					break;
				case CHUNK_MESH_TEXTURE_INFO:
					read_MESH_TEXTURE_INFO(mes);
					break;
				case CHUNK_MESH_MATRIX:
					readMatrix();
					break;
				case CHUNK_FACE_ARRAY:
					read_FACE_ARRAY(mes, head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private WB_Point[] read_POINT_ARRAY() throws Exception3ds {
			final int verts = readUnsignedShort();
			final WB_Point[] v = new WB_Point[verts];
			for (int n = 0; n < verts; n++) {
				final float x = readFloat();
				final float z = readFloat();
				final float y = readFloat();
				v[n] = new WB_Point(scale * x, scale * y, scale * z);
			}
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
			return v;
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read_TEX_VERTS() throws Exception3ds {
			final int coords = readUnsignedShort();
			for (int n = 0; n < coords; n++) {
				readFloat();
				readFloat();
			}
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param mes
		 * @throws Exception3ds
		 */
		private void read_MESH_TEXTURE_INFO(final Mesh3ds mes) throws Exception3ds {
			readUnsignedShort();
			readFloat();
			readFloat();
			if (mDecode != null) {
				mDecode.enter();
			}
			skipBytes(4 * 4 + (3 * 4 + 3) * 4);
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void readMatrix() throws Exception3ds {
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			readFloat();
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param mes
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_FACE_ARRAY(final Mesh3ds mes, final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			final int faces = readUnsignedShort();
			mes.mFace = new int[faces][3];
			for (int n = 0; n < faces; n++) {
				final int p0 = readUnsignedShort();
				final int p1 = readUnsignedShort();
				final int p2 = readUnsignedShort();
				readUnsignedShort();
				mes.mFace[n] = new int[] { p0, p1, p2 };
			}
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_MSH_MAT_GROUP:
					read_MSH_MAT_GROUP(mes);
					break;
				case CHUNK_SMOOTH_GROUP:
					read_SMOOTH_GROUP(mes, head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param mes
		 * @throws Exception3ds
		 */
		private void read_MSH_MAT_GROUP(final Mesh3ds mes) throws Exception3ds {
			read_NAME();
			final int indexes = readUnsignedShort();
			for (int n = 0; n < indexes; n++) {
				readUnsignedShort();
			}
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param mes
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_SMOOTH_GROUP(final Mesh3ds mes, final int chunk_len) throws Exception3ds {
			final int entrys = chunk_len / 4;
			for (int n = 0; n < entrys; n++) {
				readInt();
			}
			if (mDecode != null) {
				mDecode.enter();
				mDecode.leave();
			}
			if (entrys != mes.mFace.length) {
				throw new Exception3ds("SMOOTH_GROUP entrys != faces. File is probably corrupt!");
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_KFDATA(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_KFSEG:
					readInt();
					readInt();
					if (mDecode != null) {
						mDecode.enter();
						mDecode.leave();
					}
					break;
				case CHUNK_OBJECT_NODE_TAG:
					read_OBJECT_NODE_TAG(head.length - 6);
					break;
				case CHUNK_TARGET_NODE_TAG:
					read_TARGET_NODE_TAG(head.length - 6);
					break;
				case CHUNK_CAMERA_NODE_TAG:
					read_CAMERA_NODE_TAG(head.length - 6);
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_OBJECT_NODE_TAG(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			final int mesh_index = 0;
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_NODE_ID:
					read_NODE_ID();
					break;
				case CHUNK_NODE_HDR:
					read_NAME();
					mesh(mesh_index);
					readInt();
					readUnsignedShort();
					if (mDecode != null) {
						mDecode.enter();
						mDecode.leave();
					}
					// BUG: Build hierarchy here...
					break;
				case CHUNK_PIVOT:
					readFloat();
					readFloat();
					readFloat();
					if (mDecode != null) {
						mDecode.enter();
						mDecode.leave();
					}
					break;
				case CHUNK_POS_TRACK_TAG:
					read_POS_TRACK_TAG();
					break;
				case CHUNK_ROT_TRACK_TAG:
					read_ROT_TRACK_TAG();
					break;
				case CHUNK_SCL_TRACK_TAG:
					read_POS_TRACK_TAG();
					break;
				case CHUNK_MORPH_TRACK_TAG:
					read_MORPH_TRACK_TAG();
					break;
				case CHUNK_HIDE_TRACK_TAG:
					read_HIDE_TRACK_TAG();
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_TARGET_NODE_TAG(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_NODE_ID:
					read_NODE_ID();
					break;
				case CHUNK_NODE_HDR:
					read_NAME();
					readInt();
					readUnsignedShort();
					if (mDecode != null) {
						mDecode.enter();
						mDecode.leave();
					}
					// BUG: Build hierarchy here...
					break;
				case CHUNK_POS_TRACK_TAG:
					read_POS_TRACK_TAG();
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @param chunk_len
		 * @throws Exception3ds
		 */
		private void read_CAMERA_NODE_TAG(final int chunk_len) throws Exception3ds {
			final int chunk_end = filePos() + chunk_len;
			if (mDecode != null) {
				mDecode.enter();
			}
			while (filePos() < chunk_end) {
				final Head head = read_HEAD();
				switch (head.id) {
				case CHUNK_NODE_ID:
					read_NODE_ID();
					break;
				case CHUNK_NODE_HDR:
					read_NAME();
					readInt();
					readUnsignedShort();
					if (mDecode != null) {
						mDecode.enter();
						mDecode.leave();
					}
					// BUG: Build hierarchy here...
					break;
				case CHUNK_POS_TRACK_TAG:
					read_POS_TRACK_TAG();
					break;
				case CHUNK_FOV_TRACK_TAG:
					readPTrack();
					break;
				case CHUNK_ROLL_TRACK_TAG:
					readPTrack();
					break;
				default:
					skipChunk(head.length - 6);
					break;
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private int read_NODE_ID() throws Exception3ds {
			final int id = readUnsignedShort();
			if (mDecode != null) {
				mDecode.enter();
				mDecode.println("Node id: " + id);
				mDecode.leave();
			}
			return id;
		}

		/**
		 * 
		 *
		 * @return
		 * @throws Exception3ds
		 */
		private int readTrackHead() throws Exception3ds {
			int keys;
			readUnsignedShort();
			if (mDecode != null) {
			}
			skipBytes(2 * 4);
			keys = readInt();
			if (mDecode != null) {
				mDecode.println("Keys: " + keys);
			}
			return keys;
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void readSplineParams() throws Exception3ds {
			final int flags = readUnsignedShort();
			if (flags != 0) {
				if ((flags & 0x01) != 0) {
					readFloat();
					if (mDecode != null) {
					}
				}
				if ((flags & 0x02) != 0) {
					readFloat();
					if (mDecode != null) {
					}
				}
				if ((flags & 0x04) != 0) {
					readFloat();
					if (mDecode != null) {
					}
				}
				if ((flags & 0x08) != 0) {
					readFloat();
					if (mDecode != null) {
					}
				}
				if ((flags & 0x10) != 0) {
					readFloat();
					if (mDecode != null) {
					}
				}
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void readPTrack() throws Exception3ds {
			if (mDecode != null) {
				mDecode.enter();
			}
			final int keys = readTrackHead();
			for (int i = 0; i < keys; i++) {
				readInt();
				if (mDecode != null) {
				}
				readSplineParams();
				readFloat();
				if (mDecode != null) {
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read_POS_TRACK_TAG() throws Exception3ds {
			if (mDecode != null) {
				mDecode.enter();
			}
			final int keys = readTrackHead();
			for (int i = 0; i < keys; i++) {
				readInt();
				if (mDecode != null) {
				}
				readSplineParams();
				readFloat();
				readFloat();
				readFloat();
				if (mDecode != null) {
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read_ROT_TRACK_TAG() throws Exception3ds {
			if (mDecode != null) {
				mDecode.enter();
			}
			final int keys = readTrackHead();
			for (int i = 0; i < keys; i++) {
				readInt();
				if (mDecode != null) {
				}
				readSplineParams();
				readFloat();
				readFloat();
				readFloat();
				readFloat();
				if (mDecode != null) {
				}
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read_MORPH_TRACK_TAG() throws Exception3ds {
			if (mDecode != null) {
				mDecode.enter();
			}
			final int keys = readTrackHead();
			for (int i = 0; i < keys; i++) {
				readInt();
				if (mDecode != null) {
				}
				readSplineParams();
				read_NAME();
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 *
		 * @throws Exception3ds
		 */
		private void read_HIDE_TRACK_TAG() throws Exception3ds {
			if (mDecode != null) {
				mDecode.enter();
			}
			final int keys = readTrackHead();
			for (int i = 0; i < keys; i++) {
				readInt();
				if (mDecode != null) {
				}
				readSplineParams();
			}
			if (mDecode != null) {
				mDecode.leave();
			}
		}

		/**
		 * 
		 */
		static final int CHUNK_COL_RGB = 0x0010, CHUNK_COL_TRU = 0x0011, CHUNK_COL_LINRGB = 0x0012,
				CHUNK_COL_LINTRU = 0x0013, CHUNK_PERCENTW = 0x0030, // int2
																	// percentage
				CHUNK_PERCENTF = 0x0031, // float4 percentage
				CHUNK_M3DMAGIC = 0x4D4D, CHUNK_MDATA = 0x3D3D, CHUNK_MAT_ENTRY = 0xAFFF, CHUNK_MAT_NAME = 0xA000,
				CHUNK_MAT_AMBIENT = 0xA010, CHUNK_MAT_DIFFUSE = 0xA020, CHUNK_MAT_SPECULAR = 0xA030,
				CHUNK_MAT_SHININESS = 0xA041, CHUNK_MAT_TRANSPARENCY = 0xA050, CHUNK_MAT_MAP = 0xA200,
				CHUNK_MAT_MAPNAME = 0xA300, CHUNK_NAMED_OBJECT = 0x4000, CHUNK_N_TRI_OBJECT = 0x4100,
				CHUNK_POINT_ARRAY = 0x4110, CHUNK_TEX_VERTS = 0x4140, CHUNK_MESH_TEXTURE_INFO = 0x4170,
				CHUNK_MESH_MATRIX = 0x4160, CHUNK_MESH_COLOR = 0x4165, CHUNK_FACE_ARRAY = 0x4120,
				CHUNK_MSH_MAT_GROUP = 0x4130, CHUNK_SMOOTH_GROUP = 0x4150, CHUNK_N_LIGHT = 0x4600,
				CHUNK_LIT_SPOT = 0x4610, CHUNK_LIT_OFF = 0x4620, CHUNK_LIT_ATTENUATE = 0x4625,
				CHUNK_LIT_RAYSHAD = 0x4627, CHUNK_LIT_SHADOWED = 0x4630, CHUNK_LIT_LOCAL_SHADOW = 0x4640,
				CHUNK_LIT_LOCAL_SHADOW2 = 0x4641, CHUNK_LIT_SEE_CONE = 0x4650, CHUNK_LIT_SPOT_RECTANGULAR = 0x4651,
				CHUNK_LIT_SPOT_OVERSHOOT = 0x4652, CHUNK_LIT_SPOT_PROJECTOR = 0x4653, CHUNK_LIT_SPOT_RANGE = 0x4655,
				CHUNK_LIT_SPOT_ROLL = 0x4656, CHUNK_LIT_SPOT_ASPECT = 0x4657, CHUNK_LIT_RAY_BIAS = 0x4658,
				CHUNK_LIT_INNER_RANGE = 0x4659, CHUNK_LIT_OUTER_RANGE = 0x465A, CHUNK_LIT_MULTIPLIER = 0x465B,
				CHUNK_N_CAMERA = 0x4700, CHUNK_CAM_SEE_CONE = 0x4710, CHUNK_CAM_RANGES = 0x4720, CHUNK_KFDATA = 0xB000,
				CHUNK_KFSEG = 0xB008, CHUNK_OBJECT_NODE_TAG = 0xB002, CHUNK_NODE_ID = 0xB030, CHUNK_NODE_HDR = 0xB010,
				CHUNK_PIVOT = 0xB013, CHUNK_POS_TRACK_TAG = 0xB020, CHUNK_ROT_TRACK_TAG = 0xB021,
				CHUNK_SCL_TRACK_TAG = 0xB022, CHUNK_MORPH_TRACK_TAG = 0xB026, CHUNK_HIDE_TRACK_TAG = 0xB029,
				CHUNK_TARGET_NODE_TAG = 0xB004, CHUNK_CAMERA_NODE_TAG = 0xB003, CHUNK_FOV_TRACK_TAG = 0xB023,
				CHUNK_ROLL_TRACK_TAG = 0xB024;
		// CHUNK_AMBIENT_NODE_TAG = 0xB001;
	}

	/**
	 * 
	 *
	 * @param path
	 * @throws Exception3ds
	 */
	public void loadScene(final String path) throws Exception3ds {
		// Load scene
		final TextDecode3ds decode = new TextDecode3ds();
		final int level = Scene3ds.DECODE_ALL;
		final File f = new File(path);
		_scene = new Scene3ds(f, decode, level);
	}
}
