/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.processing;

import wblut.geom.WB_Vector;

/**
 * @author FVH
 *
 */
public class WB_Color {

	// http://www.cs.uml.edu/~haim/ColorCenter/ColorCenter.htm
	// ==============================================================================
	// HeatedObject ColorRGB SCALE Class
	//
	// Steve Pizer, UNC Chapel Hill (perceptually linearized)
	//
	// AGG - Alexander Gee
	//
	// 041497 - created
	// ==============================================================================
	static public int[] HEAT = new int[] { color(0, 0, 0), color(35, 0, 0), color(52, 0, 0), color(60, 0, 0),
			color(63, 1, 0), color(64, 2, 0), color(68, 5, 0), color(69, 6, 0), color(72, 8, 0), color(74, 10, 0),
			color(77, 12, 0), color(78, 14, 0), color(81, 16, 0), color(83, 17, 0), color(85, 19, 0), color(86, 20, 0),
			color(89, 22, 0), color(91, 24, 0), color(92, 25, 0), color(94, 26, 0), color(95, 28, 0), color(98, 30, 0),
			color(100, 31, 0), color(102, 33, 0), color(103, 34, 0), color(105, 35, 0), color(106, 36, 0),
			color(108, 38, 0), color(109, 39, 0), color(111, 40, 0), color(112, 42, 0), color(114, 43, 0),
			color(115, 44, 0), color(117, 45, 0), color(119, 47, 0), color(119, 47, 0), color(120, 48, 0),
			color(122, 49, 0), color(123, 51, 0), color(125, 52, 0), color(125, 52, 0), color(126, 53, 0),
			color(128, 54, 0), color(129, 56, 0), color(129, 56, 0), color(131, 57, 0), color(132, 58, 0),
			color(134, 59, 0), color(134, 59, 0), color(136, 61, 0), color(137, 62, 0), color(137, 62, 0),
			color(139, 63, 0), color(139, 63, 0), color(140, 65, 0), color(142, 66, 0), color(142, 66, 0),
			color(143, 67, 0), color(143, 67, 0), color(145, 68, 0), color(145, 68, 0), color(146, 70, 0),
			color(146, 70, 0), color(148, 71, 0), color(148, 71, 0), color(149, 72, 0), color(149, 72, 0),
			color(151, 73, 0), color(151, 73, 0), color(153, 75, 0), color(153, 75, 0), color(154, 76, 0),
			color(154, 76, 0), color(154, 76, 0), color(156, 77, 0), color(156, 77, 0), color(157, 79, 0),
			color(157, 79, 0), color(159, 80, 0), color(159, 80, 0), color(159, 80, 0), color(160, 81, 0),
			color(160, 81, 0), color(162, 82, 0), color(162, 82, 0), color(163, 84, 0), color(163, 84, 0),
			color(165, 85, 0), color(165, 85, 0), color(166, 86, 0), color(166, 86, 0), color(166, 86, 0),
			color(168, 87, 0), color(168, 87, 0), color(170, 89, 0), color(170, 89, 0), color(171, 90, 0),
			color(171, 90, 0), color(173, 91, 0), color(173, 91, 0), color(174, 93, 0), color(174, 93, 0),
			color(176, 94, 0), color(176, 94, 0), color(177, 95, 0), color(177, 95, 0), color(179, 96, 0),
			color(179, 96, 0), color(180, 98, 0), color(182, 99, 0), color(182, 99, 0), color(183, 100, 0),
			color(183, 100, 0), color(185, 102, 0), color(185, 102, 0), color(187, 103, 0), color(187, 103, 0),
			color(188, 104, 0), color(188, 104, 0), color(190, 105, 0), color(191, 107, 0), color(191, 107, 0),
			color(193, 108, 0), color(193, 108, 0), color(194, 109, 0), color(196, 110, 0), color(196, 110, 0),
			color(197, 112, 0), color(197, 112, 0), color(199, 113, 0), color(200, 114, 0), color(200, 114, 0),
			color(202, 116, 0), color(202, 116, 0), color(204, 117, 0), color(205, 118, 0), color(205, 118, 0),
			color(207, 119, 0), color(208, 121, 0), color(208, 121, 0), color(210, 122, 0), color(211, 123, 0),
			color(211, 123, 0), color(213, 124, 0), color(214, 126, 0), color(214, 126, 0), color(216, 127, 0),
			color(217, 128, 0), color(217, 128, 0), color(219, 130, 0), color(221, 131, 0), color(221, 131, 0),
			color(222, 132, 0), color(224, 133, 0), color(224, 133, 0), color(225, 135, 0), color(227, 136, 0),
			color(227, 136, 0), color(228, 137, 0), color(230, 138, 0), color(230, 138, 0), color(231, 140, 0),
			color(233, 141, 0), color(233, 141, 0), color(234, 142, 0), color(236, 144, 0), color(236, 144, 0),
			color(238, 145, 0), color(239, 146, 0), color(241, 147, 0), color(241, 147, 0), color(242, 149, 0),
			color(244, 150, 0), color(244, 150, 0), color(245, 151, 0), color(247, 153, 0), color(247, 153, 0),
			color(248, 154, 0), color(250, 155, 0), color(251, 156, 0), color(251, 156, 0), color(253, 158, 0),
			color(255, 159, 0), color(255, 159, 0), color(255, 160, 0), color(255, 161, 0), color(255, 163, 0),
			color(255, 163, 0), color(255, 164, 0), color(255, 165, 0), color(255, 167, 0), color(255, 167, 0),
			color(255, 168, 0), color(255, 169, 0), color(255, 169, 0), color(255, 170, 0), color(255, 172, 0),
			color(255, 173, 0), color(255, 173, 0), color(255, 174, 0), color(255, 175, 0), color(255, 177, 0),
			color(255, 178, 0), color(255, 179, 0), color(255, 181, 0), color(255, 181, 0), color(255, 182, 0),
			color(255, 183, 0), color(255, 184, 0), color(255, 187, 7), color(255, 188, 10), color(255, 189, 14),
			color(255, 191, 18), color(255, 192, 21), color(255, 193, 25), color(255, 195, 29), color(255, 197, 36),
			color(255, 198, 40), color(255, 200, 43), color(255, 202, 51), color(255, 204, 54), color(255, 206, 61),
			color(255, 207, 65), color(255, 210, 72), color(255, 211, 76), color(255, 214, 83), color(255, 216, 91),
			color(255, 219, 98), color(255, 221, 105), color(255, 223, 109), color(255, 225, 116), color(255, 228, 123),
			color(255, 232, 134), color(255, 234, 142), color(255, 237, 149), color(255, 239, 156),
			color(255, 240, 160), color(255, 243, 167), color(255, 246, 174), color(255, 248, 182),
			color(255, 249, 185), color(255, 252, 193), color(255, 253, 196), color(255, 255, 204),
			color(255, 255, 207), color(255, 255, 211), color(255, 255, 218), color(255, 255, 222),
			color(255, 255, 225), color(255, 255, 229), color(255, 255, 233), color(255, 255, 236),
			color(255, 255, 240), color(255, 255, 244), color(255, 255, 247), color(255, 255, 255) };
	static public int[] HOTMETAL = new int[] { color(0, 0, 0), color(2, 0, 0), color(4, 0, 0), color(6, 0, 0),
			color(8, 0, 0), color(10, 0, 0), color(12, 0, 0), color(14, 0, 0), color(16, 0, 0), color(18, 0, 0),
			color(20, 0, 0), color(22, 0, 0), color(24, 0, 0), color(26, 0, 0), color(28, 0, 0), color(30, 0, 0),
			color(32, 0, 0), color(34, 0, 0), color(36, 0, 0), color(38, 0, 0), color(40, 0, 0), color(42, 0, 0),
			color(44, 0, 0), color(46, 0, 0), color(48, 0, 0), color(50, 0, 0), color(52, 0, 0), color(54, 0, 0),
			color(56, 0, 0), color(58, 0, 0), color(60, 0, 0), color(62, 0, 0), color(64, 0, 0), color(66, 0, 0),
			color(68, 0, 0), color(70, 0, 0), color(72, 0, 0), color(74, 0, 0), color(76, 0, 0), color(78, 0, 0),
			color(80, 0, 0), color(82, 0, 0), color(84, 0, 0), color(86, 0, 0), color(88, 0, 0), color(90, 0, 0),
			color(92, 0, 0), color(94, 0, 0), color(96, 0, 0), color(98, 0, 0), color(100, 0, 0), color(102, 0, 0),
			color(104, 0, 0), color(106, 0, 0), color(108, 0, 0), color(110, 0, 0), color(112, 0, 0), color(114, 0, 0),
			color(116, 0, 0), color(118, 0, 0), color(120, 0, 0), color(122, 0, 0), color(124, 0, 0), color(126, 0, 0),
			color(128, 0, 0), color(130, 0, 0), color(132, 0, 0), color(134, 0, 0), color(136, 0, 0), color(138, 0, 0),
			color(140, 0, 0), color(142, 0, 0), color(144, 0, 0), color(146, 0, 0), color(148, 0, 0), color(150, 0, 0),
			color(152, 0, 0), color(154, 0, 0), color(156, 0, 0), color(158, 0, 0), color(160, 0, 0), color(162, 0, 0),
			color(164, 0, 0), color(166, 0, 0), color(168, 0, 0), color(170, 0, 0), color(172, 0, 0), color(174, 0, 0),
			color(176, 0, 0), color(178, 0, 0), color(180, 0, 0), color(182, 0, 0), color(184, 0, 0), color(186, 0, 0),
			color(188, 0, 0), color(190, 0, 0), color(192, 0, 0), color(194, 0, 0), color(196, 0, 0), color(198, 0, 0),
			color(200, 0, 0), color(202, 0, 0), color(204, 0, 0), color(206, 0, 0), color(208, 0, 0), color(210, 0, 0),
			color(212, 0, 0), color(214, 0, 0), color(216, 0, 0), color(218, 0, 0), color(220, 0, 0), color(222, 0, 0),
			color(224, 0, 0), color(226, 0, 0), color(228, 0, 0), color(230, 0, 0), color(232, 0, 0), color(234, 0, 0),
			color(236, 0, 0), color(238, 0, 0), color(240, 0, 0), color(242, 0, 0), color(244, 0, 0), color(246, 0, 0),
			color(248, 0, 0), color(250, 0, 0), color(252, 0, 0), color(254, 0, 0), color(255, 0, 0), color(255, 2, 0),
			color(255, 4, 0), color(255, 6, 0), color(255, 8, 0), color(255, 10, 0), color(255, 12, 0),
			color(255, 14, 0), color(255, 16, 0), color(255, 18, 0), color(255, 20, 0), color(255, 22, 0),
			color(255, 24, 0), color(255, 26, 0), color(255, 28, 0), color(255, 30, 0), color(255, 32, 0),
			color(255, 34, 0), color(255, 36, 0), color(255, 38, 0), color(255, 40, 0), color(255, 42, 0),
			color(255, 44, 0), color(255, 46, 0), color(255, 48, 0), color(255, 50, 0), color(255, 52, 0),
			color(255, 54, 0), color(255, 56, 0), color(255, 58, 0), color(255, 60, 0), color(255, 62, 0),
			color(255, 64, 0), color(255, 66, 0), color(255, 68, 0), color(255, 70, 0), color(255, 72, 0),
			color(255, 74, 0), color(255, 76, 0), color(255, 78, 0), color(255, 80, 0), color(255, 82, 0),
			color(255, 84, 0), color(255, 86, 0), color(255, 88, 0), color(255, 90, 0), color(255, 92, 0),
			color(255, 94, 0), color(255, 96, 0), color(255, 98, 0), color(255, 100, 0), color(255, 102, 0),
			color(255, 104, 0), color(255, 106, 0), color(255, 108, 0), color(255, 110, 0), color(255, 112, 0),
			color(255, 114, 0), color(255, 116, 0), color(255, 118, 0), color(255, 120, 0), color(255, 122, 0),
			color(255, 124, 0), color(255, 126, 0), color(255, 128, 4), color(255, 130, 8), color(255, 132, 12),
			color(255, 134, 16), color(255, 136, 20), color(255, 138, 24), color(255, 140, 28), color(255, 142, 32),
			color(255, 144, 36), color(255, 146, 40), color(255, 148, 44), color(255, 150, 48), color(255, 152, 52),
			color(255, 154, 56), color(255, 156, 60), color(255, 158, 64), color(255, 160, 68), color(255, 162, 72),
			color(255, 164, 76), color(255, 166, 80), color(255, 168, 84), color(255, 170, 88), color(255, 172, 92),
			color(255, 174, 96), color(255, 176, 100), color(255, 178, 104), color(255, 180, 108), color(255, 182, 112),
			color(255, 184, 116), color(255, 186, 120), color(255, 188, 124), color(255, 190, 128),
			color(255, 192, 132), color(255, 194, 136), color(255, 196, 140), color(255, 198, 144),
			color(255, 200, 148), color(255, 202, 152), color(255, 204, 156), color(255, 206, 160),
			color(255, 208, 164), color(255, 210, 168), color(255, 212, 172), color(255, 214, 176),
			color(255, 216, 180), color(255, 218, 184), color(255, 220, 188), color(255, 222, 192),
			color(255, 224, 196), color(255, 226, 200), color(255, 228, 204), color(255, 230, 208),
			color(255, 232, 212), color(255, 234, 216), color(255, 236, 220), color(255, 238, 224),
			color(255, 240, 228), color(255, 242, 232), color(255, 244, 236), color(255, 246, 240),
			color(255, 248, 244), color(255, 250, 248), color(255, 252, 252), color(255, 255, 255) };
	static public int[] HOTMETALBLUE = new int[] { color(0, 0, 0), color(0, 0, 2), color(0, 0, 4), color(0, 0, 6),
			color(0, 0, 8), color(0, 0, 10), color(0, 0, 12), color(0, 0, 14), color(0, 0, 16), color(0, 0, 17),
			color(0, 0, 19), color(0, 0, 21), color(0, 0, 23), color(0, 0, 25), color(0, 0, 27), color(0, 0, 29),
			color(0, 0, 31), color(0, 0, 33), color(0, 0, 35), color(0, 0, 37), color(0, 0, 39), color(0, 0, 41),
			color(0, 0, 43), color(0, 0, 45), color(0, 0, 47), color(0, 0, 49), color(0, 0, 51), color(0, 0, 53),
			color(0, 0, 55), color(0, 0, 57), color(0, 0, 59), color(0, 0, 61), color(0, 0, 63), color(0, 0, 65),
			color(0, 0, 67), color(0, 0, 69), color(0, 0, 71), color(0, 0, 73), color(0, 0, 75), color(0, 0, 77),
			color(0, 0, 79), color(0, 0, 81), color(0, 0, 83), color(0, 0, 84), color(0, 0, 86), color(0, 0, 88),
			color(0, 0, 90), color(0, 0, 92), color(0, 0, 94), color(0, 0, 96), color(0, 0, 98), color(0, 0, 100),
			color(0, 0, 102), color(0, 0, 104), color(0, 0, 106), color(0, 0, 108), color(0, 0, 110), color(0, 0, 112),
			color(0, 0, 114), color(0, 0, 116), color(0, 0, 117), color(0, 0, 119), color(0, 0, 121), color(0, 0, 123),
			color(0, 0, 125), color(0, 0, 127), color(0, 0, 129), color(0, 0, 131), color(0, 0, 133), color(0, 0, 135),
			color(0, 0, 137), color(0, 0, 139), color(0, 0, 141), color(0, 0, 143), color(0, 0, 145), color(0, 0, 147),
			color(0, 0, 149), color(0, 0, 151), color(0, 0, 153), color(0, 0, 155), color(0, 0, 157), color(0, 0, 159),
			color(0, 0, 161), color(0, 0, 163), color(0, 0, 165), color(0, 0, 167), color(3, 0, 169), color(6, 0, 171),
			color(9, 0, 173), color(12, 0, 175), color(15, 0, 177), color(18, 0, 179), color(21, 0, 181),
			color(24, 0, 183), color(26, 0, 184), color(29, 0, 186), color(32, 0, 188), color(35, 0, 190),
			color(38, 0, 192), color(41, 0, 194), color(44, 0, 196), color(47, 0, 198), color(50, 0, 200),
			color(52, 0, 197), color(55, 0, 194), color(57, 0, 191), color(59, 0, 188), color(62, 0, 185),
			color(64, 0, 182), color(66, 0, 179), color(69, 0, 176), color(71, 0, 174), color(74, 0, 171),
			color(76, 0, 168), color(78, 0, 165), color(81, 0, 162), color(83, 0, 159), color(85, 0, 156),
			color(88, 0, 153), color(90, 0, 150), color(93, 2, 144), color(96, 4, 138), color(99, 6, 132),
			color(102, 8, 126), color(105, 9, 121), color(108, 11, 115), color(111, 13, 109), color(114, 15, 103),
			color(116, 17, 97), color(119, 19, 91), color(122, 21, 85), color(125, 23, 79), color(128, 24, 74),
			color(131, 26, 68), color(134, 28, 62), color(137, 30, 56), color(140, 32, 50), color(143, 34, 47),
			color(146, 36, 44), color(149, 38, 41), color(152, 40, 38), color(155, 41, 35), color(158, 43, 32),
			color(161, 45, 29), color(164, 47, 26), color(166, 49, 24), color(169, 51, 21), color(172, 53, 18),
			color(175, 55, 15), color(178, 56, 12), color(181, 58, 9), color(184, 60, 6), color(187, 62, 3),
			color(190, 64, 0), color(194, 66, 0), color(198, 68, 0), color(201, 70, 0), color(205, 72, 0),
			color(209, 73, 0), color(213, 75, 0), color(217, 77, 0), color(221, 79, 0), color(224, 81, 0),
			color(228, 83, 0), color(232, 85, 0), color(236, 87, 0), color(240, 88, 0), color(244, 90, 0),
			color(247, 92, 0), color(251, 94, 0), color(255, 96, 0), color(255, 98, 3), color(255, 100, 6),
			color(255, 102, 9), color(255, 104, 12), color(255, 105, 15), color(255, 107, 18), color(255, 109, 21),
			color(255, 111, 24), color(255, 113, 26), color(255, 115, 29), color(255, 117, 32), color(255, 119, 35),
			color(255, 120, 38), color(255, 122, 41), color(255, 124, 44), color(255, 126, 47), color(255, 128, 50),
			color(255, 130, 53), color(255, 132, 56), color(255, 134, 59), color(255, 136, 62), color(255, 137, 65),
			color(255, 139, 68), color(255, 141, 71), color(255, 143, 74), color(255, 145, 76), color(255, 147, 79),
			color(255, 149, 82), color(255, 151, 85), color(255, 152, 88), color(255, 154, 91), color(255, 156, 94),
			color(255, 158, 97), color(255, 160, 100), color(255, 162, 103), color(255, 164, 106), color(255, 166, 109),
			color(255, 168, 112), color(255, 169, 115), color(255, 171, 118), color(255, 173, 121),
			color(255, 175, 124), color(255, 177, 126), color(255, 179, 129), color(255, 181, 132),
			color(255, 183, 135), color(255, 184, 138), color(255, 186, 141), color(255, 188, 144),
			color(255, 190, 147), color(255, 192, 150), color(255, 194, 153), color(255, 196, 156),
			color(255, 198, 159), color(255, 200, 162), color(255, 201, 165), color(255, 203, 168),
			color(255, 205, 171), color(255, 207, 174), color(255, 209, 176), color(255, 211, 179),
			color(255, 213, 182), color(255, 215, 185), color(255, 216, 188), color(255, 218, 191),
			color(255, 220, 194), color(255, 222, 197), color(255, 224, 200), color(255, 226, 203),
			color(255, 228, 206), color(255, 229, 210), color(255, 231, 213), color(255, 233, 216),
			color(255, 235, 219), color(255, 237, 223), color(255, 239, 226), color(255, 240, 229),
			color(255, 242, 232), color(255, 244, 236), color(255, 246, 239), color(255, 248, 242),
			color(255, 250, 245), color(255, 251, 249), color(255, 253, 252), color(255, 255, 255) };

	// ==============================================================================
	// LOCS ColorRGB SCALE Class
	//
	// Dr. Haim Levkowitz, UMass Lowell (perceptually linearized)
	//
	// AGG - Alexander Gee
	//
	// 041497 - created
	// ==============================================================================
	static public int[] OPTIMAL = new int[] { color(0, 0, 0), color(0, 0, 0), color(0, 0, 0), color(1, 0, 0),
			color(2, 0, 0), color(2, 0, 0), color(3, 0, 0), color(3, 0, 0), color(4, 0, 0), color(5, 0, 0),
			color(5, 0, 0), color(6, 0, 0), color(7, 0, 0), color(7, 0, 0), color(8, 0, 0), color(9, 0, 0),
			color(9, 0, 0), color(10, 0, 0), color(11, 0, 0), color(12, 0, 0), color(13, 0, 0), color(14, 0, 0),
			color(15, 0, 0), color(16, 0, 0), color(17, 0, 0), color(18, 0, 0), color(19, 0, 0), color(20, 0, 0),
			color(21, 0, 0), color(22, 0, 0), color(23, 0, 0), color(25, 0, 0), color(26, 0, 0), color(27, 0, 0),
			color(28, 0, 0), color(30, 0, 0), color(31, 0, 0), color(33, 0, 0), color(34, 0, 0), color(35, 0, 0),
			color(37, 0, 0), color(39, 0, 0), color(40, 0, 0), color(43, 0, 0), color(45, 0, 0), color(46, 0, 0),
			color(49, 0, 0), color(51, 0, 0), color(53, 0, 0), color(54, 0, 0), color(56, 0, 0), color(58, 0, 0),
			color(60, 0, 0), color(62, 0, 0), color(64, 0, 0), color(67, 0, 0), color(69, 0, 0), color(71, 0, 0),
			color(74, 0, 0), color(76, 0, 0), color(80, 0, 0), color(81, 0, 0), color(84, 0, 0), color(86, 0, 0),
			color(89, 0, 0), color(92, 0, 0), color(94, 0, 0), color(97, 0, 0), color(100, 0, 0), color(103, 0, 0),
			color(106, 0, 0), color(109, 0, 0), color(112, 0, 0), color(115, 0, 0), color(117, 0, 0), color(122, 0, 0),
			color(126, 0, 0), color(128, 0, 0), color(131, 0, 0), color(135, 0, 0), color(135, 0, 0), color(135, 1, 0),
			color(135, 2, 0), color(135, 3, 0), color(135, 4, 0), color(135, 6, 0), color(135, 6, 0), color(135, 8, 0),
			color(135, 9, 0), color(135, 10, 0), color(135, 11, 0), color(135, 13, 0), color(135, 13, 0),
			color(135, 15, 0), color(135, 17, 0), color(135, 17, 0), color(135, 19, 0), color(135, 21, 0),
			color(135, 22, 0), color(135, 23, 0), color(135, 25, 0), color(135, 26, 0), color(135, 27, 0),
			color(135, 29, 0), color(135, 31, 0), color(135, 32, 0), color(135, 33, 0), color(135, 35, 0),
			color(135, 36, 0), color(135, 38, 0), color(135, 40, 0), color(135, 42, 0), color(135, 44, 0),
			color(135, 46, 0), color(135, 47, 0), color(135, 49, 0), color(135, 51, 0), color(135, 52, 0),
			color(135, 54, 0), color(135, 56, 0), color(135, 57, 0), color(135, 59, 0), color(135, 62, 0),
			color(135, 63, 0), color(135, 65, 0), color(135, 67, 0), color(135, 69, 0), color(135, 72, 0),
			color(135, 73, 0), color(135, 76, 0), color(135, 78, 0), color(135, 80, 0), color(135, 82, 0),
			color(135, 84, 0), color(135, 87, 0), color(135, 88, 0), color(135, 90, 0), color(135, 93, 0),
			color(135, 95, 0), color(135, 98, 0), color(135, 101, 0), color(135, 103, 0), color(135, 106, 0),
			color(135, 107, 0), color(135, 110, 0), color(135, 113, 0), color(135, 115, 0), color(135, 118, 0),
			color(135, 121, 0), color(135, 124, 0), color(135, 127, 0), color(135, 129, 0), color(135, 133, 0),
			color(135, 135, 0), color(135, 138, 0), color(135, 141, 0), color(135, 144, 0), color(135, 148, 0),
			color(135, 150, 0), color(135, 155, 0), color(135, 157, 0), color(135, 160, 0), color(135, 163, 0),
			color(135, 166, 0), color(135, 170, 0), color(135, 174, 0), color(135, 177, 0), color(135, 180, 0),
			color(135, 184, 0), color(135, 188, 0), color(135, 192, 0), color(135, 195, 0), color(135, 200, 0),
			color(135, 203, 0), color(135, 205, 0), color(135, 210, 0), color(135, 214, 0), color(135, 218, 0),
			color(135, 222, 0), color(135, 226, 0), color(135, 231, 0), color(135, 236, 0), color(135, 239, 0),
			color(135, 244, 0), color(135, 249, 0), color(135, 254, 0), color(135, 255, 1), color(135, 255, 5),
			color(135, 255, 10), color(135, 255, 15), color(135, 255, 20), color(135, 255, 23), color(135, 255, 28),
			color(135, 255, 33), color(135, 255, 38), color(135, 255, 43), color(135, 255, 45), color(135, 255, 49),
			color(135, 255, 54), color(135, 255, 59), color(135, 255, 65), color(135, 255, 70), color(135, 255, 74),
			color(135, 255, 80), color(135, 255, 84), color(135, 255, 90), color(135, 255, 95), color(135, 255, 98),
			color(135, 255, 104), color(135, 255, 110), color(135, 255, 116), color(135, 255, 120),
			color(135, 255, 125), color(135, 255, 131), color(135, 255, 137), color(135, 255, 144),
			color(135, 255, 149), color(135, 255, 154), color(135, 255, 158), color(135, 255, 165),
			color(135, 255, 172), color(135, 255, 179), color(135, 255, 186), color(135, 255, 191),
			color(135, 255, 198), color(135, 255, 203), color(135, 255, 211), color(135, 255, 216),
			color(135, 255, 224), color(135, 255, 232), color(135, 255, 240), color(135, 255, 248),
			color(135, 255, 254), color(135, 255, 255), color(140, 255, 255), color(146, 255, 255),
			color(153, 255, 255), color(156, 255, 255), color(161, 255, 255), color(168, 255, 255),
			color(172, 255, 255), color(177, 255, 255), color(182, 255, 255), color(189, 255, 255),
			color(192, 255, 255), color(199, 255, 255), color(204, 255, 255), color(210, 255, 255),
			color(215, 255, 255), color(220, 255, 255), color(225, 255, 255), color(232, 255, 255),
			color(236, 255, 255), color(240, 255, 255), color(248, 255, 255), color(255, 255, 255) };
	// ==============================================================================
	// Gray ColorRGB SCALE Class
	//
	// Dr. Haim Levkowitz, UMass Lowell
	//
	// AGG - Alexander Gee
	//
	// 041497 - created
	// ==============================================================================
	static public int[] GRAY = new int[] {

			color(0, 0, 0), color(1, 1, 1), color(2, 2, 2), color(3, 3, 3), color(4, 4, 4), color(5, 5, 5),
			color(6, 6, 6), color(7, 7, 7), color(8, 8, 8), color(9, 9, 9), color(10, 10, 10), color(11, 11, 11),
			color(12, 12, 12), color(13, 13, 13), color(14, 14, 14), color(15, 15, 15), color(16, 16, 16),
			color(17, 17, 17), color(18, 18, 18), color(19, 19, 19), color(20, 20, 20), color(21, 21, 21),
			color(22, 22, 22), color(23, 23, 23), color(24, 24, 24), color(25, 25, 25), color(26, 26, 26),
			color(27, 27, 27), color(28, 28, 28), color(29, 29, 29), color(30, 30, 30), color(31, 31, 31),
			color(32, 32, 32), color(33, 33, 33), color(34, 34, 34), color(35, 35, 35), color(36, 36, 36),
			color(37, 37, 37), color(38, 38, 38), color(39, 39, 39), color(40, 40, 40), color(41, 41, 41),
			color(42, 42, 42), color(43, 43, 43), color(44, 44, 44), color(45, 45, 45), color(46, 46, 46),
			color(47, 47, 47), color(48, 48, 48), color(49, 49, 49), color(50, 50, 50), color(51, 51, 51),
			color(52, 52, 52), color(53, 53, 53), color(54, 54, 54), color(55, 55, 55), color(56, 56, 56),
			color(57, 57, 57), color(58, 58, 58), color(59, 59, 59), color(60, 60, 60), color(61, 61, 61),
			color(62, 62, 62), color(63, 63, 63), color(64, 64, 64), color(65, 65, 65), color(66, 66, 66),
			color(67, 67, 67), color(68, 68, 68), color(69, 69, 69), color(70, 70, 70), color(71, 71, 71),
			color(72, 72, 72), color(73, 73, 73), color(74, 74, 74), color(75, 75, 75), color(76, 76, 76),
			color(77, 77, 77), color(78, 78, 78), color(79, 79, 79), color(80, 80, 80), color(81, 81, 81),
			color(82, 82, 82), color(83, 83, 83), color(84, 84, 84), color(85, 85, 85), color(86, 86, 86),
			color(87, 87, 87), color(88, 88, 88), color(89, 89, 89), color(90, 90, 90), color(91, 91, 91),
			color(92, 92, 92), color(93, 93, 93), color(94, 94, 94), color(95, 95, 95), color(96, 96, 96),
			color(97, 97, 97), color(98, 98, 98), color(99, 99, 99), color(100, 100, 100), color(101, 101, 101),
			color(102, 102, 102), color(103, 103, 103), color(104, 104, 104), color(105, 105, 105),
			color(106, 106, 106), color(107, 107, 107), color(108, 108, 108), color(109, 109, 109),
			color(110, 110, 110), color(111, 111, 111), color(112, 112, 112), color(113, 113, 113),
			color(114, 114, 114), color(115, 115, 115), color(116, 116, 116), color(117, 117, 117),
			color(118, 118, 118), color(119, 119, 119), color(120, 120, 120), color(121, 121, 121),
			color(122, 122, 122), color(123, 123, 123), color(124, 124, 124), color(125, 125, 125),
			color(126, 126, 126), color(127, 127, 127), color(128, 128, 128), color(129, 129, 129),
			color(130, 130, 130), color(131, 131, 131), color(132, 132, 132), color(133, 133, 133),
			color(134, 134, 134), color(135, 135, 135), color(136, 136, 136), color(137, 137, 137),
			color(138, 138, 138), color(139, 139, 139), color(140, 140, 140), color(141, 141, 141),
			color(142, 142, 142), color(143, 143, 143), color(144, 144, 144), color(145, 145, 145),
			color(146, 146, 146), color(147, 147, 147), color(148, 148, 148), color(149, 149, 149),
			color(150, 150, 150), color(151, 151, 151), color(152, 152, 152), color(153, 153, 153),
			color(154, 154, 154), color(155, 155, 155), color(156, 156, 156), color(157, 157, 157),
			color(158, 158, 158), color(159, 159, 159), color(160, 160, 160), color(161, 161, 161),
			color(162, 162, 162), color(163, 163, 163), color(164, 164, 164), color(165, 165, 165),
			color(166, 166, 166), color(167, 167, 167), color(168, 168, 168), color(169, 169, 169),
			color(170, 170, 170), color(171, 171, 171), color(172, 172, 172), color(173, 173, 173),
			color(174, 174, 174), color(175, 175, 175), color(176, 176, 176), color(177, 177, 177),
			color(178, 178, 178), color(179, 179, 179), color(180, 180, 180), color(181, 181, 181),
			color(182, 182, 182), color(183, 183, 183), color(184, 184, 184), color(185, 185, 185),
			color(186, 186, 186), color(187, 187, 187), color(188, 188, 188), color(189, 189, 189),
			color(190, 190, 190), color(191, 191, 191), color(192, 192, 192), color(193, 193, 193),
			color(194, 194, 194), color(195, 195, 195), color(196, 196, 196), color(197, 197, 197),
			color(198, 198, 198), color(199, 199, 199), color(200, 200, 200), color(201, 201, 201),
			color(202, 202, 202), color(203, 203, 203), color(204, 204, 204), color(205, 205, 205),
			color(206, 206, 206), color(207, 207, 207), color(208, 208, 208), color(209, 209, 209),
			color(210, 210, 210), color(211, 211, 211), color(212, 212, 212), color(213, 213, 213),
			color(214, 214, 214), color(215, 215, 215), color(216, 216, 216), color(217, 217, 217),
			color(218, 218, 218), color(219, 219, 219), color(220, 220, 220), color(221, 221, 221),
			color(222, 222, 222), color(223, 223, 223), color(224, 224, 224), color(225, 225, 225),
			color(226, 226, 226), color(227, 227, 227), color(228, 228, 228), color(229, 229, 229),
			color(230, 230, 230), color(231, 231, 231), color(232, 232, 232), color(233, 233, 233),
			color(234, 234, 234), color(235, 235, 235), color(236, 236, 236), color(237, 237, 237),
			color(238, 238, 238), color(239, 239, 239), color(240, 240, 240), color(241, 241, 241),
			color(242, 242, 242), color(243, 243, 243), color(244, 244, 244), color(245, 245, 245),
			color(246, 246, 246), color(247, 247, 247), color(248, 248, 248), color(249, 249, 249),
			color(250, 250, 250), color(251, 251, 251), color(252, 252, 252), color(253, 253, 253),
			color(254, 254, 254), color(255, 255, 255) };
	// ==============================================================================
	// LinGray ColorRGB SCALE Class
	//
	// Dr. Haim Levkowitz, UMass Lowell (perceptually linearized)
	//
	// AGG - Alexander Gee
	//
	// 041497 - created
	// ==============================================================================
	static public int[] LINGRAY = new int[] {

			color(0, 0, 0), color(0, 0, 0), color(0, 0, 0), color(0, 0, 0), color(0, 0, 0), color(0, 0, 0),
			color(0, 0, 0), color(1, 1, 1), color(1, 1, 1), color(1, 1, 1), color(1, 1, 1), color(1, 1, 1),
			color(1, 1, 1), color(1, 1, 1), color(1, 1, 1), color(1, 1, 1), color(2, 2, 2), color(2, 2, 2),
			color(2, 2, 2), color(2, 2, 2), color(2, 2, 2), color(2, 2, 2), color(2, 2, 2), color(3, 3, 3),
			color(3, 3, 3), color(3, 3, 3), color(3, 3, 3), color(3, 3, 3), color(3, 3, 3), color(3, 3, 3),
			color(4, 4, 4), color(4, 4, 4), color(4, 4, 4), color(4, 4, 4), color(4, 4, 4), color(5, 5, 5),
			color(5, 5, 5), color(5, 5, 5), color(5, 5, 5), color(5, 5, 5), color(6, 6, 6), color(6, 6, 6),
			color(6, 6, 6), color(6, 6, 6), color(6, 6, 6), color(7, 7, 7), color(7, 7, 7), color(7, 7, 7),
			color(7, 7, 7), color(7, 7, 7), color(8, 8, 8), color(8, 8, 8), color(9, 9, 9), color(9, 9, 9),
			color(9, 9, 9), color(9, 9, 9), color(10, 10, 10), color(10, 10, 10), color(10, 10, 10), color(10, 10, 10),
			color(10, 10, 10), color(11, 11, 11), color(11, 11, 11), color(12, 12, 12), color(12, 12, 12),
			color(12, 12, 12), color(13, 13, 13), color(13, 13, 13), color(14, 14, 14), color(14, 14, 14),
			color(15, 15, 15), color(15, 15, 15), color(15, 15, 15), color(16, 16, 16), color(16, 16, 16),
			color(17, 17, 17), color(17, 17, 17), color(18, 18, 18), color(18, 18, 18), color(19, 19, 19),
			color(19, 19, 19), color(19, 19, 19), color(19, 19, 19), color(19, 19, 19), color(20, 20, 20),
			color(20, 20, 20), color(22, 22, 22), color(22, 22, 22), color(22, 22, 22), color(23, 23, 23),
			color(23, 23, 23), color(24, 24, 24), color(24, 24, 24), color(26, 26, 26), color(26, 26, 26),
			color(26, 26, 26), color(27, 27, 27), color(27, 27, 27), color(29, 29, 29), color(29, 29, 29),
			color(30, 30, 30), color(30, 30, 30), color(32, 32, 32), color(32, 32, 32), color(32, 32, 32),
			color(32, 32, 32), color(32, 32, 32), color(34, 34, 34), color(34, 34, 34), color(35, 35, 35),
			color(35, 35, 35), color(35, 35, 35), color(37, 37, 37), color(37, 37, 37), color(39, 39, 39),
			color(39, 39, 39), color(41, 41, 41), color(41, 41, 41), color(41, 41, 41), color(43, 43, 43),
			color(43, 43, 43), color(45, 45, 45), color(45, 45, 45), color(46, 46, 46), color(46, 46, 46),
			color(46, 46, 46), color(47, 47, 47), color(47, 47, 47), color(49, 49, 49), color(49, 49, 49),
			color(51, 51, 51), color(51, 51, 51), color(52, 52, 52), color(52, 52, 52), color(52, 52, 52),
			color(54, 54, 54), color(54, 54, 54), color(56, 56, 56), color(56, 56, 56), color(59, 59, 59),
			color(59, 59, 59), color(59, 59, 59), color(61, 61, 61), color(61, 61, 61), color(64, 64, 64),
			color(64, 64, 64), color(67, 67, 67), color(67, 67, 67), color(67, 67, 67), color(69, 69, 69),
			color(69, 69, 69), color(72, 72, 72), color(72, 72, 72), color(75, 75, 75), color(75, 75, 75),
			color(76, 76, 76), color(76, 76, 76), color(76, 76, 76), color(78, 78, 78), color(78, 78, 78),
			color(81, 81, 81), color(81, 81, 81), color(84, 84, 84), color(84, 84, 84), color(84, 84, 84),
			color(87, 87, 87), color(87, 87, 87), color(91, 91, 91), color(91, 91, 91), color(94, 94, 94),
			color(94, 94, 94), color(94, 94, 94), color(97, 97, 97), color(97, 97, 97), color(101, 101, 101),
			color(101, 101, 101), color(104, 104, 104), color(104, 104, 104), color(107, 107, 107),
			color(107, 107, 107), color(107, 107, 107), color(108, 108, 108), color(108, 108, 108),
			color(112, 112, 112), color(112, 112, 112), color(116, 116, 116), color(116, 116, 116),
			color(116, 116, 116), color(120, 120, 120), color(120, 120, 120), color(124, 124, 124),
			color(124, 124, 124), color(128, 128, 128), color(128, 128, 128), color(128, 128, 128),
			color(132, 132, 132), color(132, 132, 132), color(136, 136, 136), color(136, 136, 136),
			color(141, 141, 141), color(141, 141, 141), color(145, 145, 145), color(145, 145, 145),
			color(145, 145, 145), color(147, 147, 147), color(147, 147, 147), color(150, 150, 150),
			color(150, 150, 150), color(154, 154, 154), color(154, 154, 154), color(154, 154, 154),
			color(159, 159, 159), color(159, 159, 159), color(164, 164, 164), color(164, 164, 164),
			color(169, 169, 169), color(169, 169, 169), color(169, 169, 169), color(174, 174, 174),
			color(174, 174, 174), color(179, 179, 179), color(179, 179, 179), color(185, 185, 185),
			color(185, 185, 185), color(190, 190, 190), color(190, 190, 190), color(190, 190, 190),
			color(195, 195, 195), color(195, 195, 195), color(195, 195, 195), color(195, 195, 195),
			color(201, 201, 201), color(201, 201, 201), color(201, 201, 201), color(207, 207, 207),
			color(207, 207, 207), color(212, 212, 212), color(212, 212, 212), color(218, 218, 218),
			color(218, 218, 218), color(218, 218, 218), color(224, 224, 224), color(224, 224, 224),
			color(230, 230, 230), color(230, 230, 230), color(237, 237, 237), color(237, 237, 237),
			color(243, 243, 243), color(243, 243, 243), color(243, 243, 243), color(249, 249, 249),
			color(249, 249, 249), color(252, 252, 252), color(252, 252, 252), color(252, 252, 252),
			color(255, 255, 255) };
	// ==============================================================================
	// Rainbow ColorRGB SCALE Class
	//
	// Steve Pizer, UNC Chapel Hill (perceptually linearized)
	//
	// AGG - Alexander Gee
	//
	// 041497 - created
	// ==============================================================================
	static public int[] PLRAINBOW = new int[] { color(0, 0, 0), color(45, 0, 36), color(56, 0, 46), color(60, 0, 49),
			color(67, 0, 54), color(70, 0, 59), color(71, 0, 61), color(75, 0, 68), color(74, 0, 73), color(74, 0, 77),
			color(73, 0, 81), color(71, 0, 87), color(69, 1, 90), color(68, 2, 94), color(66, 3, 97), color(63, 6, 102),
			color(61, 7, 106), color(58, 10, 109), color(56, 12, 113), color(53, 15, 116), color(48, 18, 119),
			color(47, 20, 121), color(44, 23, 124), color(41, 27, 128), color(40, 28, 129), color(37, 32, 132),
			color(34, 36, 134), color(29, 43, 137), color(25, 52, 138), color(24, 57, 139), color(24, 62, 141),
			color(24, 64, 142), color(23, 65, 142), color(23, 69, 143), color(23, 71, 142), color(23, 71, 142),
			color(23, 73, 142), color(23, 75, 142), color(23, 75, 142), color(23, 78, 142), color(23, 80, 142),
			color(23, 80, 142), color(23, 82, 141), color(23, 85, 141), color(23, 85, 141), color(23, 87, 140),
			color(23, 87, 140), color(24, 90, 140), color(24, 90, 140), color(24, 93, 139), color(24, 93, 139),
			color(24, 93, 139), color(24, 93, 139), color(24, 97, 139), color(24, 97, 139), color(25, 101, 138),
			color(25, 101, 138), color(25, 104, 137), color(25, 104, 137), color(25, 104, 137), color(26, 108, 137),
			color(26, 108, 137), color(27, 111, 136), color(27, 111, 136), color(27, 111, 136), color(27, 115, 135),
			color(27, 115, 135), color(28, 118, 134), color(28, 118, 134), color(29, 122, 133), color(29, 122, 133),
			color(29, 122, 133), color(29, 122, 133), color(29, 125, 132), color(29, 125, 132), color(30, 128, 131),
			color(30, 128, 131), color(31, 131, 130), color(31, 131, 130), color(31, 131, 130), color(32, 134, 128),
			color(32, 134, 128), color(33, 137, 127), color(33, 137, 127), color(33, 137, 127), color(34, 140, 125),
			color(34, 140, 125), color(35, 142, 123), color(35, 142, 123), color(36, 145, 121), color(36, 145, 121),
			color(36, 145, 121), color(37, 147, 118), color(37, 147, 118), color(38, 150, 116), color(38, 150, 116),
			color(40, 152, 113), color(40, 152, 113), color(41, 154, 111), color(41, 154, 111), color(42, 156, 108),
			color(42, 156, 108), color(43, 158, 106), color(43, 158, 106), color(43, 158, 106), color(45, 160, 104),
			color(45, 160, 104), color(46, 162, 101), color(46, 162, 101), color(48, 164, 99), color(48, 164, 99),
			color(50, 166, 97), color(50, 166, 97), color(51, 168, 95), color(53, 170, 93), color(53, 170, 93),
			color(53, 170, 93), color(55, 172, 91), color(55, 172, 91), color(57, 174, 88), color(57, 174, 88),
			color(59, 175, 86), color(62, 177, 84), color(64, 178, 82), color(64, 178, 82), color(67, 180, 80),
			color(67, 180, 80), color(69, 181, 79), color(72, 183, 77), color(72, 183, 77), color(72, 183, 77),
			color(75, 184, 76), color(77, 186, 74), color(80, 187, 73), color(83, 189, 72), color(87, 190, 72),
			color(91, 191, 71), color(95, 192, 70), color(99, 193, 70), color(103, 194, 70), color(107, 195, 70),
			color(111, 196, 70), color(111, 196, 70), color(115, 196, 70), color(119, 197, 70), color(123, 197, 70),
			color(130, 198, 71), color(133, 199, 71), color(137, 199, 72), color(140, 199, 72), color(143, 199, 73),
			color(143, 199, 73), color(147, 199, 73), color(150, 199, 74), color(153, 199, 74), color(156, 199, 75),
			color(160, 200, 76), color(167, 200, 78), color(170, 200, 79), color(173, 200, 79), color(173, 200, 79),
			color(177, 200, 80), color(180, 200, 81), color(183, 199, 82), color(186, 199, 82), color(190, 199, 83),
			color(196, 199, 85), color(199, 198, 85), color(199, 198, 85), color(203, 198, 86), color(206, 197, 87),
			color(212, 197, 89), color(215, 196, 90), color(218, 195, 91), color(224, 194, 94), color(224, 194, 94),
			color(230, 193, 96), color(233, 192, 98), color(236, 190, 100), color(238, 189, 104), color(240, 188, 106),
			color(240, 188, 106), color(242, 187, 110), color(244, 185, 114), color(245, 184, 116),
			color(247, 183, 120), color(248, 182, 123), color(248, 182, 123), color(250, 181, 125),
			color(251, 180, 128), color(252, 180, 130), color(253, 180, 133), color(253, 180, 133),
			color(254, 180, 134), color(254, 179, 138), color(255, 179, 142), color(255, 179, 145),
			color(255, 179, 145), color(255, 179, 152), color(255, 180, 161), color(255, 180, 164),
			color(255, 180, 167), color(255, 180, 167), color(255, 181, 169), color(255, 181, 170),
			color(255, 182, 173), color(255, 183, 176), color(255, 183, 176), color(255, 184, 179),
			color(255, 185, 179), color(255, 185, 182), color(255, 186, 182), color(255, 186, 182),
			color(255, 187, 185), color(255, 188, 185), color(255, 189, 188), color(255, 189, 188),
			color(255, 190, 188), color(255, 191, 191), color(255, 192, 191), color(255, 194, 194),
			color(255, 194, 194), color(255, 197, 197), color(255, 198, 198), color(255, 200, 200),
			color(255, 201, 201), color(255, 201, 201), color(255, 202, 202), color(255, 203, 203),
			color(255, 205, 205), color(255, 206, 206), color(255, 206, 206), color(255, 208, 208),
			color(255, 209, 209), color(255, 211, 211), color(255, 215, 215), color(255, 216, 216),
			color(255, 216, 216), color(255, 218, 218), color(255, 219, 219), color(255, 221, 221),
			color(255, 223, 223), color(255, 226, 226), color(255, 228, 228), color(255, 230, 230),
			color(255, 230, 230), color(255, 232, 232), color(255, 235, 235), color(255, 237, 237),
			color(255, 240, 240), color(255, 243, 243), color(255, 246, 246), color(255, 249, 249),
			color(255, 251, 251), color(255, 253, 253), color(255, 255, 255) };
	static public int[] SPECTRUM = new int[256];
	static {
		for (int i = 0; i < 256; i++) {
			SPECTRUM[i] = spectralColor(400.0 + 300.0 / 255.0 * i);
		}
	}
	/*
	 * New matplotlib colormaps by Nathaniel J. Smith, Stefan van der Walt, and
	 * (in the case of viridis) Eric Firing.
	 *
	 * This file and the colormaps in it are released under the CC0 license /
	 * public domain dedication. We would appreciate credit if you use or
	 * redistribute these colormaps, but do not impose any legal restrictions.
	 *
	 * To the extent possible under law, the persons who associated CC0 with
	 * mpl-colormaps have waived all copyright and related or neighboring rights
	 * to mpl-colormaps.
	 *
	 * You should have received a copy of the CC0 legalcode along with this
	 * work. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
	 */

	static public int[] MAGMA = new int[] { normColor(0.001462, 0.000466, 0.013866),
			normColor(0.002258, 0.001295, 0.018331), normColor(0.003279, 0.002305, 0.023708),
			normColor(0.004512, 0.003490, 0.029965), normColor(0.005950, 0.004843, 0.037130),
			normColor(0.007588, 0.006356, 0.044973), normColor(0.009426, 0.008022, 0.052844),
			normColor(0.011465, 0.009828, 0.060750), normColor(0.013708, 0.011771, 0.068667),
			normColor(0.016156, 0.013840, 0.076603), normColor(0.018815, 0.016026, 0.084584),
			normColor(0.021692, 0.018320, 0.092610), normColor(0.024792, 0.020715, 0.100676),
			normColor(0.028123, 0.023201, 0.108787), normColor(0.031696, 0.025765, 0.116965),
			normColor(0.035520, 0.028397, 0.125209), normColor(0.039608, 0.031090, 0.133515),
			normColor(0.043830, 0.033830, 0.141886), normColor(0.048062, 0.036607, 0.150327),
			normColor(0.052320, 0.039407, 0.158841), normColor(0.056615, 0.042160, 0.167446),
			normColor(0.060949, 0.044794, 0.176129), normColor(0.065330, 0.047318, 0.184892),
			normColor(0.069764, 0.049726, 0.193735), normColor(0.074257, 0.052017, 0.202660),
			normColor(0.078815, 0.054184, 0.211667), normColor(0.083446, 0.056225, 0.220755),
			normColor(0.088155, 0.058133, 0.229922), normColor(0.092949, 0.059904, 0.239164),
			normColor(0.097833, 0.061531, 0.248477), normColor(0.102815, 0.063010, 0.257854),
			normColor(0.107899, 0.064335, 0.267289), normColor(0.113094, 0.065492, 0.276784),
			normColor(0.118405, 0.066479, 0.286321), normColor(0.123833, 0.067295, 0.295879),
			normColor(0.129380, 0.067935, 0.305443), normColor(0.135053, 0.068391, 0.315000),
			normColor(0.140858, 0.068654, 0.324538), normColor(0.146785, 0.068738, 0.334011),
			normColor(0.152839, 0.068637, 0.343404), normColor(0.159018, 0.068354, 0.352688),
			normColor(0.165308, 0.067911, 0.361816), normColor(0.171713, 0.067305, 0.370771),
			normColor(0.178212, 0.066576, 0.379497), normColor(0.184801, 0.065732, 0.387973),
			normColor(0.191460, 0.064818, 0.396152), normColor(0.198177, 0.063862, 0.404009),
			normColor(0.204935, 0.062907, 0.411514), normColor(0.211718, 0.061992, 0.418647),
			normColor(0.218512, 0.061158, 0.425392), normColor(0.225302, 0.060445, 0.431742),
			normColor(0.232077, 0.059889, 0.437695), normColor(0.238826, 0.059517, 0.443256),
			normColor(0.245543, 0.059352, 0.448436), normColor(0.252220, 0.059415, 0.453248),
			normColor(0.258857, 0.059706, 0.457710), normColor(0.265447, 0.060237, 0.461840),
			normColor(0.271994, 0.060994, 0.465660), normColor(0.278493, 0.061978, 0.469190),
			normColor(0.284951, 0.063168, 0.472451), normColor(0.291366, 0.064553, 0.475462),
			normColor(0.297740, 0.066117, 0.478243), normColor(0.304081, 0.067835, 0.480812),
			normColor(0.310382, 0.069702, 0.483186), normColor(0.316654, 0.071690, 0.485380),
			normColor(0.322899, 0.073782, 0.487408), normColor(0.329114, 0.075972, 0.489287),
			normColor(0.335308, 0.078236, 0.491024), normColor(0.341482, 0.080564, 0.492631),
			normColor(0.347636, 0.082946, 0.494121), normColor(0.353773, 0.085373, 0.495501),
			normColor(0.359898, 0.087831, 0.496778), normColor(0.366012, 0.090314, 0.497960),
			normColor(0.372116, 0.092816, 0.499053), normColor(0.378211, 0.095332, 0.500067),
			normColor(0.384299, 0.097855, 0.501002), normColor(0.390384, 0.100379, 0.501864),
			normColor(0.396467, 0.102902, 0.502658), normColor(0.402548, 0.105420, 0.503386),
			normColor(0.408629, 0.107930, 0.504052), normColor(0.414709, 0.110431, 0.504662),
			normColor(0.420791, 0.112920, 0.505215), normColor(0.426877, 0.115395, 0.505714),
			normColor(0.432967, 0.117855, 0.506160), normColor(0.439062, 0.120298, 0.506555),
			normColor(0.445163, 0.122724, 0.506901), normColor(0.451271, 0.125132, 0.507198),
			normColor(0.457386, 0.127522, 0.507448), normColor(0.463508, 0.129893, 0.507652),
			normColor(0.469640, 0.132245, 0.507809), normColor(0.475780, 0.134577, 0.507921),
			normColor(0.481929, 0.136891, 0.507989), normColor(0.488088, 0.139186, 0.508011),
			normColor(0.494258, 0.141462, 0.507988), normColor(0.500438, 0.143719, 0.507920),
			normColor(0.506629, 0.145958, 0.507806), normColor(0.512831, 0.148179, 0.507648),
			normColor(0.519045, 0.150383, 0.507443), normColor(0.525270, 0.152569, 0.507192),
			normColor(0.531507, 0.154739, 0.506895), normColor(0.537755, 0.156894, 0.506551),
			normColor(0.544015, 0.159033, 0.506159), normColor(0.550287, 0.161158, 0.505719),
			normColor(0.556571, 0.163269, 0.505230), normColor(0.562866, 0.165368, 0.504692),
			normColor(0.569172, 0.167454, 0.504105), normColor(0.575490, 0.169530, 0.503466),
			normColor(0.581819, 0.171596, 0.502777), normColor(0.588158, 0.173652, 0.502035),
			normColor(0.594508, 0.175701, 0.501241), normColor(0.600868, 0.177743, 0.500394),
			normColor(0.607238, 0.179779, 0.499492), normColor(0.613617, 0.181811, 0.498536),
			normColor(0.620005, 0.183840, 0.497524), normColor(0.626401, 0.185867, 0.496456),
			normColor(0.632805, 0.187893, 0.495332), normColor(0.639216, 0.189921, 0.494150),
			normColor(0.645633, 0.191952, 0.492910), normColor(0.652056, 0.193986, 0.491611),
			normColor(0.658483, 0.196027, 0.490253), normColor(0.664915, 0.198075, 0.488836),
			normColor(0.671349, 0.200133, 0.487358), normColor(0.677786, 0.202203, 0.485819),
			normColor(0.684224, 0.204286, 0.484219), normColor(0.690661, 0.206384, 0.482558),
			normColor(0.697098, 0.208501, 0.480835), normColor(0.703532, 0.210638, 0.479049),
			normColor(0.709962, 0.212797, 0.477201), normColor(0.716387, 0.214982, 0.475290),
			normColor(0.722805, 0.217194, 0.473316), normColor(0.729216, 0.219437, 0.471279),
			normColor(0.735616, 0.221713, 0.469180), normColor(0.742004, 0.224025, 0.467018),
			normColor(0.748378, 0.226377, 0.464794), normColor(0.754737, 0.228772, 0.462509),
			normColor(0.761077, 0.231214, 0.460162), normColor(0.767398, 0.233705, 0.457755),
			normColor(0.773695, 0.236249, 0.455289), normColor(0.779968, 0.238851, 0.452765),
			normColor(0.786212, 0.241514, 0.450184), normColor(0.792427, 0.244242, 0.447543),
			normColor(0.798608, 0.247040, 0.444848), normColor(0.804752, 0.249911, 0.442102),
			normColor(0.810855, 0.252861, 0.439305), normColor(0.816914, 0.255895, 0.436461),
			normColor(0.822926, 0.259016, 0.433573), normColor(0.828886, 0.262229, 0.430644),
			normColor(0.834791, 0.265540, 0.427671), normColor(0.840636, 0.268953, 0.424666),
			normColor(0.846416, 0.272473, 0.421631), normColor(0.852126, 0.276106, 0.418573),
			normColor(0.857763, 0.279857, 0.415496), normColor(0.863320, 0.283729, 0.412403),
			normColor(0.868793, 0.287728, 0.409303), normColor(0.874176, 0.291859, 0.406205),
			normColor(0.879464, 0.296125, 0.403118), normColor(0.884651, 0.300530, 0.400047),
			normColor(0.889731, 0.305079, 0.397002), normColor(0.894700, 0.309773, 0.393995),
			normColor(0.899552, 0.314616, 0.391037), normColor(0.904281, 0.319610, 0.388137),
			normColor(0.908884, 0.324755, 0.385308), normColor(0.913354, 0.330052, 0.382563),
			normColor(0.917689, 0.335500, 0.379915), normColor(0.921884, 0.341098, 0.377376),
			normColor(0.925937, 0.346844, 0.374959), normColor(0.929845, 0.352734, 0.372677),
			normColor(0.933606, 0.358764, 0.370541), normColor(0.937221, 0.364929, 0.368567),
			normColor(0.940687, 0.371224, 0.366762), normColor(0.944006, 0.377643, 0.365136),
			normColor(0.947180, 0.384178, 0.363701), normColor(0.950210, 0.390820, 0.362468),
			normColor(0.953099, 0.397563, 0.361438), normColor(0.955849, 0.404400, 0.360619),
			normColor(0.958464, 0.411324, 0.360014), normColor(0.960949, 0.418323, 0.359630),
			normColor(0.963310, 0.425390, 0.359469), normColor(0.965549, 0.432519, 0.359529),
			normColor(0.967671, 0.439703, 0.359810), normColor(0.969680, 0.446936, 0.360311),
			normColor(0.971582, 0.454210, 0.361030), normColor(0.973381, 0.461520, 0.361965),
			normColor(0.975082, 0.468861, 0.363111), normColor(0.976690, 0.476226, 0.364466),
			normColor(0.978210, 0.483612, 0.366025), normColor(0.979645, 0.491014, 0.367783),
			normColor(0.981000, 0.498428, 0.369734), normColor(0.982279, 0.505851, 0.371874),
			normColor(0.983485, 0.513280, 0.374198), normColor(0.984622, 0.520713, 0.376698),
			normColor(0.985693, 0.528148, 0.379371), normColor(0.986700, 0.535582, 0.382210),
			normColor(0.987646, 0.543015, 0.385210), normColor(0.988533, 0.550446, 0.388365),
			normColor(0.989363, 0.557873, 0.391671), normColor(0.990138, 0.565296, 0.395122),
			normColor(0.990871, 0.572706, 0.398714), normColor(0.991558, 0.580107, 0.402441),
			normColor(0.992196, 0.587502, 0.406299), normColor(0.992785, 0.594891, 0.410283),
			normColor(0.993326, 0.602275, 0.414390), normColor(0.993834, 0.609644, 0.418613),
			normColor(0.994309, 0.616999, 0.422950), normColor(0.994738, 0.624350, 0.427397),
			normColor(0.995122, 0.631696, 0.431951), normColor(0.995480, 0.639027, 0.436607),
			normColor(0.995810, 0.646344, 0.441361), normColor(0.996096, 0.653659, 0.446213),
			normColor(0.996341, 0.660969, 0.451160), normColor(0.996580, 0.668256, 0.456192),
			normColor(0.996775, 0.675541, 0.461314), normColor(0.996925, 0.682828, 0.466526),
			normColor(0.997077, 0.690088, 0.471811), normColor(0.997186, 0.697349, 0.477182),
			normColor(0.997254, 0.704611, 0.482635), normColor(0.997325, 0.711848, 0.488154),
			normColor(0.997351, 0.719089, 0.493755), normColor(0.997351, 0.726324, 0.499428),
			normColor(0.997341, 0.733545, 0.505167), normColor(0.997285, 0.740772, 0.510983),
			normColor(0.997228, 0.747981, 0.516859), normColor(0.997138, 0.755190, 0.522806),
			normColor(0.997019, 0.762398, 0.528821), normColor(0.996898, 0.769591, 0.534892),
			normColor(0.996727, 0.776795, 0.541039), normColor(0.996571, 0.783977, 0.547233),
			normColor(0.996369, 0.791167, 0.553499), normColor(0.996162, 0.798348, 0.559820),
			normColor(0.995932, 0.805527, 0.566202), normColor(0.995680, 0.812706, 0.572645),
			normColor(0.995424, 0.819875, 0.579140), normColor(0.995131, 0.827052, 0.585701),
			normColor(0.994851, 0.834213, 0.592307), normColor(0.994524, 0.841387, 0.598983),
			normColor(0.994222, 0.848540, 0.605696), normColor(0.993866, 0.855711, 0.612482),
			normColor(0.993545, 0.862859, 0.619299), normColor(0.993170, 0.870024, 0.626189),
			normColor(0.992831, 0.877168, 0.633109), normColor(0.992440, 0.884330, 0.640099),
			normColor(0.992089, 0.891470, 0.647116), normColor(0.991688, 0.898627, 0.654202),
			normColor(0.991332, 0.905763, 0.661309), normColor(0.990930, 0.912915, 0.668481),
			normColor(0.990570, 0.920049, 0.675675), normColor(0.990175, 0.927196, 0.682926),
			normColor(0.989815, 0.934329, 0.690198), normColor(0.989434, 0.941470, 0.697519),
			normColor(0.989077, 0.948604, 0.704863), normColor(0.988717, 0.955742, 0.712242),
			normColor(0.988367, 0.962878, 0.719649), normColor(0.988033, 0.970012, 0.727077),
			normColor(0.987691, 0.977154, 0.734536), normColor(0.987387, 0.984288, 0.742002),
			normColor(0.987053, 0.991438, 0.749504) };
	static public int[] INFERNO = new int[] { normColor(0.001462, 0.000466, 0.013866),
			normColor(0.002267, 0.001270, 0.018570), normColor(0.003299, 0.002249, 0.024239),
			normColor(0.004547, 0.003392, 0.030909), normColor(0.006006, 0.004692, 0.038558),
			normColor(0.007676, 0.006136, 0.046836), normColor(0.009561, 0.007713, 0.055143),
			normColor(0.011663, 0.009417, 0.063460), normColor(0.013995, 0.011225, 0.071862),
			normColor(0.016561, 0.013136, 0.080282), normColor(0.019373, 0.015133, 0.088767),
			normColor(0.022447, 0.017199, 0.097327), normColor(0.025793, 0.019331, 0.105930),
			normColor(0.029432, 0.021503, 0.114621), normColor(0.033385, 0.023702, 0.123397),
			normColor(0.037668, 0.025921, 0.132232), normColor(0.042253, 0.028139, 0.141141),
			normColor(0.046915, 0.030324, 0.150164), normColor(0.051644, 0.032474, 0.159254),
			normColor(0.056449, 0.034569, 0.168414), normColor(0.061340, 0.036590, 0.177642),
			normColor(0.066331, 0.038504, 0.186962), normColor(0.071429, 0.040294, 0.196354),
			normColor(0.076637, 0.041905, 0.205799), normColor(0.081962, 0.043328, 0.215289),
			normColor(0.087411, 0.044556, 0.224813), normColor(0.092990, 0.045583, 0.234358),
			normColor(0.098702, 0.046402, 0.243904), normColor(0.104551, 0.047008, 0.253430),
			normColor(0.110536, 0.047399, 0.262912), normColor(0.116656, 0.047574, 0.272321),
			normColor(0.122908, 0.047536, 0.281624), normColor(0.129285, 0.047293, 0.290788),
			normColor(0.135778, 0.046856, 0.299776), normColor(0.142378, 0.046242, 0.308553),
			normColor(0.149073, 0.045468, 0.317085), normColor(0.155850, 0.044559, 0.325338),
			normColor(0.162689, 0.043554, 0.333277), normColor(0.169575, 0.042489, 0.340874),
			normColor(0.176493, 0.041402, 0.348111), normColor(0.183429, 0.040329, 0.354971),
			normColor(0.190367, 0.039309, 0.361447), normColor(0.197297, 0.038400, 0.367535),
			normColor(0.204209, 0.037632, 0.373238), normColor(0.211095, 0.037030, 0.378563),
			normColor(0.217949, 0.036615, 0.383522), normColor(0.224763, 0.036405, 0.388129),
			normColor(0.231538, 0.036405, 0.392400), normColor(0.238273, 0.036621, 0.396353),
			normColor(0.244967, 0.037055, 0.400007), normColor(0.251620, 0.037705, 0.403378),
			normColor(0.258234, 0.038571, 0.406485), normColor(0.264810, 0.039647, 0.409345),
			normColor(0.271347, 0.040922, 0.411976), normColor(0.277850, 0.042353, 0.414392),
			normColor(0.284321, 0.043933, 0.416608), normColor(0.290763, 0.045644, 0.418637),
			normColor(0.297178, 0.047470, 0.420491), normColor(0.303568, 0.049396, 0.422182),
			normColor(0.309935, 0.051407, 0.423721), normColor(0.316282, 0.053490, 0.425116),
			normColor(0.322610, 0.055634, 0.426377), normColor(0.328921, 0.057827, 0.427511),
			normColor(0.335217, 0.060060, 0.428524), normColor(0.341500, 0.062325, 0.429425),
			normColor(0.347771, 0.064616, 0.430217), normColor(0.354032, 0.066925, 0.430906),
			normColor(0.360284, 0.069247, 0.431497), normColor(0.366529, 0.071579, 0.431994),
			normColor(0.372768, 0.073915, 0.432400), normColor(0.379001, 0.076253, 0.432719),
			normColor(0.385228, 0.078591, 0.432955), normColor(0.391453, 0.080927, 0.433109),
			normColor(0.397674, 0.083257, 0.433183), normColor(0.403894, 0.085580, 0.433179),
			normColor(0.410113, 0.087896, 0.433098), normColor(0.416331, 0.090203, 0.432943),
			normColor(0.422549, 0.092501, 0.432714), normColor(0.428768, 0.094790, 0.432412),
			normColor(0.434987, 0.097069, 0.432039), normColor(0.441207, 0.099338, 0.431594),
			normColor(0.447428, 0.101597, 0.431080), normColor(0.453651, 0.103848, 0.430498),
			normColor(0.459875, 0.106089, 0.429846), normColor(0.466100, 0.108322, 0.429125),
			normColor(0.472328, 0.110547, 0.428334), normColor(0.478558, 0.112764, 0.427475),
			normColor(0.484789, 0.114974, 0.426548), normColor(0.491022, 0.117179, 0.425552),
			normColor(0.497257, 0.119379, 0.424488), normColor(0.503493, 0.121575, 0.423356),
			normColor(0.509730, 0.123769, 0.422156), normColor(0.515967, 0.125960, 0.420887),
			normColor(0.522206, 0.128150, 0.419549), normColor(0.528444, 0.130341, 0.418142),
			normColor(0.534683, 0.132534, 0.416667), normColor(0.540920, 0.134729, 0.415123),
			normColor(0.547157, 0.136929, 0.413511), normColor(0.553392, 0.139134, 0.411829),
			normColor(0.559624, 0.141346, 0.410078), normColor(0.565854, 0.143567, 0.408258),
			normColor(0.572081, 0.145797, 0.406369), normColor(0.578304, 0.148039, 0.404411),
			normColor(0.584521, 0.150294, 0.402385), normColor(0.590734, 0.152563, 0.400290),
			normColor(0.596940, 0.154848, 0.398125), normColor(0.603139, 0.157151, 0.395891),
			normColor(0.609330, 0.159474, 0.393589), normColor(0.615513, 0.161817, 0.391219),
			normColor(0.621685, 0.164184, 0.388781), normColor(0.627847, 0.166575, 0.386276),
			normColor(0.633998, 0.168992, 0.383704), normColor(0.640135, 0.171438, 0.381065),
			normColor(0.646260, 0.173914, 0.378359), normColor(0.652369, 0.176421, 0.375586),
			normColor(0.658463, 0.178962, 0.372748), normColor(0.664540, 0.181539, 0.369846),
			normColor(0.670599, 0.184153, 0.366879), normColor(0.676638, 0.186807, 0.363849),
			normColor(0.682656, 0.189501, 0.360757), normColor(0.688653, 0.192239, 0.357603),
			normColor(0.694627, 0.195021, 0.354388), normColor(0.700576, 0.197851, 0.351113),
			normColor(0.706500, 0.200728, 0.347777), normColor(0.712396, 0.203656, 0.344383),
			normColor(0.718264, 0.206636, 0.340931), normColor(0.724103, 0.209670, 0.337424),
			normColor(0.729909, 0.212759, 0.333861), normColor(0.735683, 0.215906, 0.330245),
			normColor(0.741423, 0.219112, 0.326576), normColor(0.747127, 0.222378, 0.322856),
			normColor(0.752794, 0.225706, 0.319085), normColor(0.758422, 0.229097, 0.315266),
			normColor(0.764010, 0.232554, 0.311399), normColor(0.769556, 0.236077, 0.307485),
			normColor(0.775059, 0.239667, 0.303526), normColor(0.780517, 0.243327, 0.299523),
			normColor(0.785929, 0.247056, 0.295477), normColor(0.791293, 0.250856, 0.291390),
			normColor(0.796607, 0.254728, 0.287264), normColor(0.801871, 0.258674, 0.283099),
			normColor(0.807082, 0.262692, 0.278898), normColor(0.812239, 0.266786, 0.274661),
			normColor(0.817341, 0.270954, 0.270390), normColor(0.822386, 0.275197, 0.266085),
			normColor(0.827372, 0.279517, 0.261750), normColor(0.832299, 0.283913, 0.257383),
			normColor(0.837165, 0.288385, 0.252988), normColor(0.841969, 0.292933, 0.248564),
			normColor(0.846709, 0.297559, 0.244113), normColor(0.851384, 0.302260, 0.239636),
			normColor(0.855992, 0.307038, 0.235133), normColor(0.860533, 0.311892, 0.230606),
			normColor(0.865006, 0.316822, 0.226055), normColor(0.869409, 0.321827, 0.221482),
			normColor(0.873741, 0.326906, 0.216886), normColor(0.878001, 0.332060, 0.212268),
			normColor(0.882188, 0.337287, 0.207628), normColor(0.886302, 0.342586, 0.202968),
			normColor(0.890341, 0.347957, 0.198286), normColor(0.894305, 0.353399, 0.193584),
			normColor(0.898192, 0.358911, 0.188860), normColor(0.902003, 0.364492, 0.184116),
			normColor(0.905735, 0.370140, 0.179350), normColor(0.909390, 0.375856, 0.174563),
			normColor(0.912966, 0.381636, 0.169755), normColor(0.916462, 0.387481, 0.164924),
			normColor(0.919879, 0.393389, 0.160070), normColor(0.923215, 0.399359, 0.155193),
			normColor(0.926470, 0.405389, 0.150292), normColor(0.929644, 0.411479, 0.145367),
			normColor(0.932737, 0.417627, 0.140417), normColor(0.935747, 0.423831, 0.135440),
			normColor(0.938675, 0.430091, 0.130438), normColor(0.941521, 0.436405, 0.125409),
			normColor(0.944285, 0.442772, 0.120354), normColor(0.946965, 0.449191, 0.115272),
			normColor(0.949562, 0.455660, 0.110164), normColor(0.952075, 0.462178, 0.105031),
			normColor(0.954506, 0.468744, 0.099874), normColor(0.956852, 0.475356, 0.094695),
			normColor(0.959114, 0.482014, 0.089499), normColor(0.961293, 0.488716, 0.084289),
			normColor(0.963387, 0.495462, 0.079073), normColor(0.965397, 0.502249, 0.073859),
			normColor(0.967322, 0.509078, 0.068659), normColor(0.969163, 0.515946, 0.063488),
			normColor(0.970919, 0.522853, 0.058367), normColor(0.972590, 0.529798, 0.053324),
			normColor(0.974176, 0.536780, 0.048392), normColor(0.975677, 0.543798, 0.043618),
			normColor(0.977092, 0.550850, 0.039050), normColor(0.978422, 0.557937, 0.034931),
			normColor(0.979666, 0.565057, 0.031409), normColor(0.980824, 0.572209, 0.028508),
			normColor(0.981895, 0.579392, 0.026250), normColor(0.982881, 0.586606, 0.024661),
			normColor(0.983779, 0.593849, 0.023770), normColor(0.984591, 0.601122, 0.023606),
			normColor(0.985315, 0.608422, 0.024202), normColor(0.985952, 0.615750, 0.025592),
			normColor(0.986502, 0.623105, 0.027814), normColor(0.986964, 0.630485, 0.030908),
			normColor(0.987337, 0.637890, 0.034916), normColor(0.987622, 0.645320, 0.039886),
			normColor(0.987819, 0.652773, 0.045581), normColor(0.987926, 0.660250, 0.051750),
			normColor(0.987945, 0.667748, 0.058329), normColor(0.987874, 0.675267, 0.065257),
			normColor(0.987714, 0.682807, 0.072489), normColor(0.987464, 0.690366, 0.079990),
			normColor(0.987124, 0.697944, 0.087731), normColor(0.986694, 0.705540, 0.095694),
			normColor(0.986175, 0.713153, 0.103863), normColor(0.985566, 0.720782, 0.112229),
			normColor(0.984865, 0.728427, 0.120785), normColor(0.984075, 0.736087, 0.129527),
			normColor(0.983196, 0.743758, 0.138453), normColor(0.982228, 0.751442, 0.147565),
			normColor(0.981173, 0.759135, 0.156863), normColor(0.980032, 0.766837, 0.166353),
			normColor(0.978806, 0.774545, 0.176037), normColor(0.977497, 0.782258, 0.185923),
			normColor(0.976108, 0.789974, 0.196018), normColor(0.974638, 0.797692, 0.206332),
			normColor(0.973088, 0.805409, 0.216877), normColor(0.971468, 0.813122, 0.227658),
			normColor(0.969783, 0.820825, 0.238686), normColor(0.968041, 0.828515, 0.249972),
			normColor(0.966243, 0.836191, 0.261534), normColor(0.964394, 0.843848, 0.273391),
			normColor(0.962517, 0.851476, 0.285546), normColor(0.960626, 0.859069, 0.298010),
			normColor(0.958720, 0.866624, 0.310820), normColor(0.956834, 0.874129, 0.323974),
			normColor(0.954997, 0.881569, 0.337475), normColor(0.953215, 0.888942, 0.351369),
			normColor(0.951546, 0.896226, 0.365627), normColor(0.950018, 0.903409, 0.380271),
			normColor(0.948683, 0.910473, 0.395289), normColor(0.947594, 0.917399, 0.410665),
			normColor(0.946809, 0.924168, 0.426373), normColor(0.946392, 0.930761, 0.442367),
			normColor(0.946403, 0.937159, 0.458592), normColor(0.946903, 0.943348, 0.474970),
			normColor(0.947937, 0.949318, 0.491426), normColor(0.949545, 0.955063, 0.507860),
			normColor(0.951740, 0.960587, 0.524203), normColor(0.954529, 0.965896, 0.540361),
			normColor(0.957896, 0.971003, 0.556275), normColor(0.961812, 0.975924, 0.571925),
			normColor(0.966249, 0.980678, 0.587206), normColor(0.971162, 0.985282, 0.602154),
			normColor(0.976511, 0.989753, 0.616760), normColor(0.982257, 0.994109, 0.631017),
			normColor(0.988362, 0.998364, 0.644924) };
	static public int[] PLASMA = new int[] { normColor(0.050383, 0.029803, 0.527975),
			normColor(0.063536, 0.028426, 0.533124), normColor(0.075353, 0.027206, 0.538007),
			normColor(0.086222, 0.026125, 0.542658), normColor(0.096379, 0.025165, 0.547103),
			normColor(0.105980, 0.024309, 0.551368), normColor(0.115124, 0.023556, 0.555468),
			normColor(0.123903, 0.022878, 0.559423), normColor(0.132381, 0.022258, 0.563250),
			normColor(0.140603, 0.021687, 0.566959), normColor(0.148607, 0.021154, 0.570562),
			normColor(0.156421, 0.020651, 0.574065), normColor(0.164070, 0.020171, 0.577478),
			normColor(0.171574, 0.019706, 0.580806), normColor(0.178950, 0.019252, 0.584054),
			normColor(0.186213, 0.018803, 0.587228), normColor(0.193374, 0.018354, 0.590330),
			normColor(0.200445, 0.017902, 0.593364), normColor(0.207435, 0.017442, 0.596333),
			normColor(0.214350, 0.016973, 0.599239), normColor(0.221197, 0.016497, 0.602083),
			normColor(0.227983, 0.016007, 0.604867), normColor(0.234715, 0.015502, 0.607592),
			normColor(0.241396, 0.014979, 0.610259), normColor(0.248032, 0.014439, 0.612868),
			normColor(0.254627, 0.013882, 0.615419), normColor(0.261183, 0.013308, 0.617911),
			normColor(0.267703, 0.012716, 0.620346), normColor(0.274191, 0.012109, 0.622722),
			normColor(0.280648, 0.011488, 0.625038), normColor(0.287076, 0.010855, 0.627295),
			normColor(0.293478, 0.010213, 0.629490), normColor(0.299855, 0.009561, 0.631624),
			normColor(0.306210, 0.008902, 0.633694), normColor(0.312543, 0.008239, 0.635700),
			normColor(0.318856, 0.007576, 0.637640), normColor(0.325150, 0.006915, 0.639512),
			normColor(0.331426, 0.006261, 0.641316), normColor(0.337683, 0.005618, 0.643049),
			normColor(0.343925, 0.004991, 0.644710), normColor(0.350150, 0.004382, 0.646298),
			normColor(0.356359, 0.003798, 0.647810), normColor(0.362553, 0.003243, 0.649245),
			normColor(0.368733, 0.002724, 0.650601), normColor(0.374897, 0.002245, 0.651876),
			normColor(0.381047, 0.001814, 0.653068), normColor(0.387183, 0.001434, 0.654177),
			normColor(0.393304, 0.001114, 0.655199), normColor(0.399411, 0.000859, 0.656133),
			normColor(0.405503, 0.000678, 0.656977), normColor(0.411580, 0.000577, 0.657730),
			normColor(0.417642, 0.000564, 0.658390), normColor(0.423689, 0.000646, 0.658956),
			normColor(0.429719, 0.000831, 0.659425), normColor(0.435734, 0.001127, 0.659797),
			normColor(0.441732, 0.001540, 0.660069), normColor(0.447714, 0.002080, 0.660240),
			normColor(0.453677, 0.002755, 0.660310), normColor(0.459623, 0.003574, 0.660277),
			normColor(0.465550, 0.004545, 0.660139), normColor(0.471457, 0.005678, 0.659897),
			normColor(0.477344, 0.006980, 0.659549), normColor(0.483210, 0.008460, 0.659095),
			normColor(0.489055, 0.010127, 0.658534), normColor(0.494877, 0.011990, 0.657865),
			normColor(0.500678, 0.014055, 0.657088), normColor(0.506454, 0.016333, 0.656202),
			normColor(0.512206, 0.018833, 0.655209), normColor(0.517933, 0.021563, 0.654109),
			normColor(0.523633, 0.024532, 0.652901), normColor(0.529306, 0.027747, 0.651586),
			normColor(0.534952, 0.031217, 0.650165), normColor(0.540570, 0.034950, 0.648640),
			normColor(0.546157, 0.038954, 0.647010), normColor(0.551715, 0.043136, 0.645277),
			normColor(0.557243, 0.047331, 0.643443), normColor(0.562738, 0.051545, 0.641509),
			normColor(0.568201, 0.055778, 0.639477), normColor(0.573632, 0.060028, 0.637349),
			normColor(0.579029, 0.064296, 0.635126), normColor(0.584391, 0.068579, 0.632812),
			normColor(0.589719, 0.072878, 0.630408), normColor(0.595011, 0.077190, 0.627917),
			normColor(0.600266, 0.081516, 0.625342), normColor(0.605485, 0.085854, 0.622686),
			normColor(0.610667, 0.090204, 0.619951), normColor(0.615812, 0.094564, 0.617140),
			normColor(0.620919, 0.098934, 0.614257), normColor(0.625987, 0.103312, 0.611305),
			normColor(0.631017, 0.107699, 0.608287), normColor(0.636008, 0.112092, 0.605205),
			normColor(0.640959, 0.116492, 0.602065), normColor(0.645872, 0.120898, 0.598867),
			normColor(0.650746, 0.125309, 0.595617), normColor(0.655580, 0.129725, 0.592317),
			normColor(0.660374, 0.134144, 0.588971), normColor(0.665129, 0.138566, 0.585582),
			normColor(0.669845, 0.142992, 0.582154), normColor(0.674522, 0.147419, 0.578688),
			normColor(0.679160, 0.151848, 0.575189), normColor(0.683758, 0.156278, 0.571660),
			normColor(0.688318, 0.160709, 0.568103), normColor(0.692840, 0.165141, 0.564522),
			normColor(0.697324, 0.169573, 0.560919), normColor(0.701769, 0.174005, 0.557296),
			normColor(0.706178, 0.178437, 0.553657), normColor(0.710549, 0.182868, 0.550004),
			normColor(0.714883, 0.187299, 0.546338), normColor(0.719181, 0.191729, 0.542663),
			normColor(0.723444, 0.196158, 0.538981), normColor(0.727670, 0.200586, 0.535293),
			normColor(0.731862, 0.205013, 0.531601), normColor(0.736019, 0.209439, 0.527908),
			normColor(0.740143, 0.213864, 0.524216), normColor(0.744232, 0.218288, 0.520524),
			normColor(0.748289, 0.222711, 0.516834), normColor(0.752312, 0.227133, 0.513149),
			normColor(0.756304, 0.231555, 0.509468), normColor(0.760264, 0.235976, 0.505794),
			normColor(0.764193, 0.240396, 0.502126), normColor(0.768090, 0.244817, 0.498465),
			normColor(0.771958, 0.249237, 0.494813), normColor(0.775796, 0.253658, 0.491171),
			normColor(0.779604, 0.258078, 0.487539), normColor(0.783383, 0.262500, 0.483918),
			normColor(0.787133, 0.266922, 0.480307), normColor(0.790855, 0.271345, 0.476706),
			normColor(0.794549, 0.275770, 0.473117), normColor(0.798216, 0.280197, 0.469538),
			normColor(0.801855, 0.284626, 0.465971), normColor(0.805467, 0.289057, 0.462415),
			normColor(0.809052, 0.293491, 0.458870), normColor(0.812612, 0.297928, 0.455338),
			normColor(0.816144, 0.302368, 0.451816), normColor(0.819651, 0.306812, 0.448306),
			normColor(0.823132, 0.311261, 0.444806), normColor(0.826588, 0.315714, 0.441316),
			normColor(0.830018, 0.320172, 0.437836), normColor(0.833422, 0.324635, 0.434366),
			normColor(0.836801, 0.329105, 0.430905), normColor(0.840155, 0.333580, 0.427455),
			normColor(0.843484, 0.338062, 0.424013), normColor(0.846788, 0.342551, 0.420579),
			normColor(0.850066, 0.347048, 0.417153), normColor(0.853319, 0.351553, 0.413734),
			normColor(0.856547, 0.356066, 0.410322), normColor(0.859750, 0.360588, 0.406917),
			normColor(0.862927, 0.365119, 0.403519), normColor(0.866078, 0.369660, 0.400126),
			normColor(0.869203, 0.374212, 0.396738), normColor(0.872303, 0.378774, 0.393355),
			normColor(0.875376, 0.383347, 0.389976), normColor(0.878423, 0.387932, 0.386600),
			normColor(0.881443, 0.392529, 0.383229), normColor(0.884436, 0.397139, 0.379860),
			normColor(0.887402, 0.401762, 0.376494), normColor(0.890340, 0.406398, 0.373130),
			normColor(0.893250, 0.411048, 0.369768), normColor(0.896131, 0.415712, 0.366407),
			normColor(0.898984, 0.420392, 0.363047), normColor(0.901807, 0.425087, 0.359688),
			normColor(0.904601, 0.429797, 0.356329), normColor(0.907365, 0.434524, 0.352970),
			normColor(0.910098, 0.439268, 0.349610), normColor(0.912800, 0.444029, 0.346251),
			normColor(0.915471, 0.448807, 0.342890), normColor(0.918109, 0.453603, 0.339529),
			normColor(0.920714, 0.458417, 0.336166), normColor(0.923287, 0.463251, 0.332801),
			normColor(0.925825, 0.468103, 0.329435), normColor(0.928329, 0.472975, 0.326067),
			normColor(0.930798, 0.477867, 0.322697), normColor(0.933232, 0.482780, 0.319325),
			normColor(0.935630, 0.487712, 0.315952), normColor(0.937990, 0.492667, 0.312575),
			normColor(0.940313, 0.497642, 0.309197), normColor(0.942598, 0.502639, 0.305816),
			normColor(0.944844, 0.507658, 0.302433), normColor(0.947051, 0.512699, 0.299049),
			normColor(0.949217, 0.517763, 0.295662), normColor(0.951344, 0.522850, 0.292275),
			normColor(0.953428, 0.527960, 0.288883), normColor(0.955470, 0.533093, 0.285490),
			normColor(0.957469, 0.538250, 0.282096), normColor(0.959424, 0.543431, 0.278701),
			normColor(0.961336, 0.548636, 0.275305), normColor(0.963203, 0.553865, 0.271909),
			normColor(0.965024, 0.559118, 0.268513), normColor(0.966798, 0.564396, 0.265118),
			normColor(0.968526, 0.569700, 0.261721), normColor(0.970205, 0.575028, 0.258325),
			normColor(0.971835, 0.580382, 0.254931), normColor(0.973416, 0.585761, 0.251540),
			normColor(0.974947, 0.591165, 0.248151), normColor(0.976428, 0.596595, 0.244767),
			normColor(0.977856, 0.602051, 0.241387), normColor(0.979233, 0.607532, 0.238013),
			normColor(0.980556, 0.613039, 0.234646), normColor(0.981826, 0.618572, 0.231287),
			normColor(0.983041, 0.624131, 0.227937), normColor(0.984199, 0.629718, 0.224595),
			normColor(0.985301, 0.635330, 0.221265), normColor(0.986345, 0.640969, 0.217948),
			normColor(0.987332, 0.646633, 0.214648), normColor(0.988260, 0.652325, 0.211364),
			normColor(0.989128, 0.658043, 0.208100), normColor(0.989935, 0.663787, 0.204859),
			normColor(0.990681, 0.669558, 0.201642), normColor(0.991365, 0.675355, 0.198453),
			normColor(0.991985, 0.681179, 0.195295), normColor(0.992541, 0.687030, 0.192170),
			normColor(0.993032, 0.692907, 0.189084), normColor(0.993456, 0.698810, 0.186041),
			normColor(0.993814, 0.704741, 0.183043), normColor(0.994103, 0.710698, 0.180097),
			normColor(0.994324, 0.716681, 0.177208), normColor(0.994474, 0.722691, 0.174381),
			normColor(0.994553, 0.728728, 0.171622), normColor(0.994561, 0.734791, 0.168938),
			normColor(0.994495, 0.740880, 0.166335), normColor(0.994355, 0.746995, 0.163821),
			normColor(0.994141, 0.753137, 0.161404), normColor(0.993851, 0.759304, 0.159092),
			normColor(0.993482, 0.765499, 0.156891), normColor(0.993033, 0.771720, 0.154808),
			normColor(0.992505, 0.777967, 0.152855), normColor(0.991897, 0.784239, 0.151042),
			normColor(0.991209, 0.790537, 0.149377), normColor(0.990439, 0.796859, 0.147870),
			normColor(0.989587, 0.803205, 0.146529), normColor(0.988648, 0.809579, 0.145357),
			normColor(0.987621, 0.815978, 0.144363), normColor(0.986509, 0.822401, 0.143557),
			normColor(0.985314, 0.828846, 0.142945), normColor(0.984031, 0.835315, 0.142528),
			normColor(0.982653, 0.841812, 0.142303), normColor(0.981190, 0.848329, 0.142279),
			normColor(0.979644, 0.854866, 0.142453), normColor(0.977995, 0.861432, 0.142808),
			normColor(0.976265, 0.868016, 0.143351), normColor(0.974443, 0.874622, 0.144061),
			normColor(0.972530, 0.881250, 0.144923), normColor(0.970533, 0.887896, 0.145919),
			normColor(0.968443, 0.894564, 0.147014), normColor(0.966271, 0.901249, 0.148180),
			normColor(0.964021, 0.907950, 0.149370), normColor(0.961681, 0.914672, 0.150520),
			normColor(0.959276, 0.921407, 0.151566), normColor(0.956808, 0.928152, 0.152409),
			normColor(0.954287, 0.934908, 0.152921), normColor(0.951726, 0.941671, 0.152925),
			normColor(0.949151, 0.948435, 0.152178), normColor(0.946602, 0.955190, 0.150328),
			normColor(0.944152, 0.961916, 0.146861), normColor(0.941896, 0.968590, 0.140956),
			normColor(0.940015, 0.975158, 0.131326) };
	static public int[] VIRIDIS = new int[] { normColor(0.267004, 0.004874, 0.329415),
			normColor(0.268510, 0.009605, 0.335427), normColor(0.269944, 0.014625, 0.341379),
			normColor(0.271305, 0.019942, 0.347269), normColor(0.272594, 0.025563, 0.353093),
			normColor(0.273809, 0.031497, 0.358853), normColor(0.274952, 0.037752, 0.364543),
			normColor(0.276022, 0.044167, 0.370164), normColor(0.277018, 0.050344, 0.375715),
			normColor(0.277941, 0.056324, 0.381191), normColor(0.278791, 0.062145, 0.386592),
			normColor(0.279566, 0.067836, 0.391917), normColor(0.280267, 0.073417, 0.397163),
			normColor(0.280894, 0.078907, 0.402329), normColor(0.281446, 0.084320, 0.407414),
			normColor(0.281924, 0.089666, 0.412415), normColor(0.282327, 0.094955, 0.417331),
			normColor(0.282656, 0.100196, 0.422160), normColor(0.282910, 0.105393, 0.426902),
			normColor(0.283091, 0.110553, 0.431554), normColor(0.283197, 0.115680, 0.436115),
			normColor(0.283229, 0.120777, 0.440584), normColor(0.283187, 0.125848, 0.444960),
			normColor(0.283072, 0.130895, 0.449241), normColor(0.282884, 0.135920, 0.453427),
			normColor(0.282623, 0.140926, 0.457517), normColor(0.282290, 0.145912, 0.461510),
			normColor(0.281887, 0.150881, 0.465405), normColor(0.281412, 0.155834, 0.469201),
			normColor(0.280868, 0.160771, 0.472899), normColor(0.280255, 0.165693, 0.476498),
			normColor(0.279574, 0.170599, 0.479997), normColor(0.278826, 0.175490, 0.483397),
			normColor(0.278012, 0.180367, 0.486697), normColor(0.277134, 0.185228, 0.489898),
			normColor(0.276194, 0.190074, 0.493001), normColor(0.275191, 0.194905, 0.496005),
			normColor(0.274128, 0.199721, 0.498911), normColor(0.273006, 0.204520, 0.501721),
			normColor(0.271828, 0.209303, 0.504434), normColor(0.270595, 0.214069, 0.507052),
			normColor(0.269308, 0.218818, 0.509577), normColor(0.267968, 0.223549, 0.512008),
			normColor(0.266580, 0.228262, 0.514349), normColor(0.265145, 0.232956, 0.516599),
			normColor(0.263663, 0.237631, 0.518762), normColor(0.262138, 0.242286, 0.520837),
			normColor(0.260571, 0.246922, 0.522828), normColor(0.258965, 0.251537, 0.524736),
			normColor(0.257322, 0.256130, 0.526563), normColor(0.255645, 0.260703, 0.528312),
			normColor(0.253935, 0.265254, 0.529983), normColor(0.252194, 0.269783, 0.531579),
			normColor(0.250425, 0.274290, 0.533103), normColor(0.248629, 0.278775, 0.534556),
			normColor(0.246811, 0.283237, 0.535941), normColor(0.244972, 0.287675, 0.537260),
			normColor(0.243113, 0.292092, 0.538516), normColor(0.241237, 0.296485, 0.539709),
			normColor(0.239346, 0.300855, 0.540844), normColor(0.237441, 0.305202, 0.541921),
			normColor(0.235526, 0.309527, 0.542944), normColor(0.233603, 0.313828, 0.543914),
			normColor(0.231674, 0.318106, 0.544834), normColor(0.229739, 0.322361, 0.545706),
			normColor(0.227802, 0.326594, 0.546532), normColor(0.225863, 0.330805, 0.547314),
			normColor(0.223925, 0.334994, 0.548053), normColor(0.221989, 0.339161, 0.548752),
			normColor(0.220057, 0.343307, 0.549413), normColor(0.218130, 0.347432, 0.550038),
			normColor(0.216210, 0.351535, 0.550627), normColor(0.214298, 0.355619, 0.551184),
			normColor(0.212395, 0.359683, 0.551710), normColor(0.210503, 0.363727, 0.552206),
			normColor(0.208623, 0.367752, 0.552675), normColor(0.206756, 0.371758, 0.553117),
			normColor(0.204903, 0.375746, 0.553533), normColor(0.203063, 0.379716, 0.553925),
			normColor(0.201239, 0.383670, 0.554294), normColor(0.199430, 0.387607, 0.554642),
			normColor(0.197636, 0.391528, 0.554969), normColor(0.195860, 0.395433, 0.555276),
			normColor(0.194100, 0.399323, 0.555565), normColor(0.192357, 0.403199, 0.555836),
			normColor(0.190631, 0.407061, 0.556089), normColor(0.188923, 0.410910, 0.556326),
			normColor(0.187231, 0.414746, 0.556547), normColor(0.185556, 0.418570, 0.556753),
			normColor(0.183898, 0.422383, 0.556944), normColor(0.182256, 0.426184, 0.557120),
			normColor(0.180629, 0.429975, 0.557282), normColor(0.179019, 0.433756, 0.557430),
			normColor(0.177423, 0.437527, 0.557565), normColor(0.175841, 0.441290, 0.557685),
			normColor(0.174274, 0.445044, 0.557792), normColor(0.172719, 0.448791, 0.557885),
			normColor(0.171176, 0.452530, 0.557965), normColor(0.169646, 0.456262, 0.558030),
			normColor(0.168126, 0.459988, 0.558082), normColor(0.166617, 0.463708, 0.558119),
			normColor(0.165117, 0.467423, 0.558141), normColor(0.163625, 0.471133, 0.558148),
			normColor(0.162142, 0.474838, 0.558140), normColor(0.160665, 0.478540, 0.558115),
			normColor(0.159194, 0.482237, 0.558073), normColor(0.157729, 0.485932, 0.558013),
			normColor(0.156270, 0.489624, 0.557936), normColor(0.154815, 0.493313, 0.557840),
			normColor(0.153364, 0.497000, 0.557724), normColor(0.151918, 0.500685, 0.557587),
			normColor(0.150476, 0.504369, 0.557430), normColor(0.149039, 0.508051, 0.557250),
			normColor(0.147607, 0.511733, 0.557049), normColor(0.146180, 0.515413, 0.556823),
			normColor(0.144759, 0.519093, 0.556572), normColor(0.143343, 0.522773, 0.556295),
			normColor(0.141935, 0.526453, 0.555991), normColor(0.140536, 0.530132, 0.555659),
			normColor(0.139147, 0.533812, 0.555298), normColor(0.137770, 0.537492, 0.554906),
			normColor(0.136408, 0.541173, 0.554483), normColor(0.135066, 0.544853, 0.554029),
			normColor(0.133743, 0.548535, 0.553541), normColor(0.132444, 0.552216, 0.553018),
			normColor(0.131172, 0.555899, 0.552459), normColor(0.129933, 0.559582, 0.551864),
			normColor(0.128729, 0.563265, 0.551229), normColor(0.127568, 0.566949, 0.550556),
			normColor(0.126453, 0.570633, 0.549841), normColor(0.125394, 0.574318, 0.549086),
			normColor(0.124395, 0.578002, 0.548287), normColor(0.123463, 0.581687, 0.547445),
			normColor(0.122606, 0.585371, 0.546557), normColor(0.121831, 0.589055, 0.545623),
			normColor(0.121148, 0.592739, 0.544641), normColor(0.120565, 0.596422, 0.543611),
			normColor(0.120092, 0.600104, 0.542530), normColor(0.119738, 0.603785, 0.541400),
			normColor(0.119512, 0.607464, 0.540218), normColor(0.119423, 0.611141, 0.538982),
			normColor(0.119483, 0.614817, 0.537692), normColor(0.119699, 0.618490, 0.536347),
			normColor(0.120081, 0.622161, 0.534946), normColor(0.120638, 0.625828, 0.533488),
			normColor(0.121380, 0.629492, 0.531973), normColor(0.122312, 0.633153, 0.530398),
			normColor(0.123444, 0.636809, 0.528763), normColor(0.124780, 0.640461, 0.527068),
			normColor(0.126326, 0.644107, 0.525311), normColor(0.128087, 0.647749, 0.523491),
			normColor(0.130067, 0.651384, 0.521608), normColor(0.132268, 0.655014, 0.519661),
			normColor(0.134692, 0.658636, 0.517649), normColor(0.137339, 0.662252, 0.515571),
			normColor(0.140210, 0.665859, 0.513427), normColor(0.143303, 0.669459, 0.511215),
			normColor(0.146616, 0.673050, 0.508936), normColor(0.150148, 0.676631, 0.506589),
			normColor(0.153894, 0.680203, 0.504172), normColor(0.157851, 0.683765, 0.501686),
			normColor(0.162016, 0.687316, 0.499129), normColor(0.166383, 0.690856, 0.496502),
			normColor(0.170948, 0.694384, 0.493803), normColor(0.175707, 0.697900, 0.491033),
			normColor(0.180653, 0.701402, 0.488189), normColor(0.185783, 0.704891, 0.485273),
			normColor(0.191090, 0.708366, 0.482284), normColor(0.196571, 0.711827, 0.479221),
			normColor(0.202219, 0.715272, 0.476084), normColor(0.208030, 0.718701, 0.472873),
			normColor(0.214000, 0.722114, 0.469588), normColor(0.220124, 0.725509, 0.466226),
			normColor(0.226397, 0.728888, 0.462789), normColor(0.232815, 0.732247, 0.459277),
			normColor(0.239374, 0.735588, 0.455688), normColor(0.246070, 0.738910, 0.452024),
			normColor(0.252899, 0.742211, 0.448284), normColor(0.259857, 0.745492, 0.444467),
			normColor(0.266941, 0.748751, 0.440573), normColor(0.274149, 0.751988, 0.436601),
			normColor(0.281477, 0.755203, 0.432552), normColor(0.288921, 0.758394, 0.428426),
			normColor(0.296479, 0.761561, 0.424223), normColor(0.304148, 0.764704, 0.419943),
			normColor(0.311925, 0.767822, 0.415586), normColor(0.319809, 0.770914, 0.411152),
			normColor(0.327796, 0.773980, 0.406640), normColor(0.335885, 0.777018, 0.402049),
			normColor(0.344074, 0.780029, 0.397381), normColor(0.352360, 0.783011, 0.392636),
			normColor(0.360741, 0.785964, 0.387814), normColor(0.369214, 0.788888, 0.382914),
			normColor(0.377779, 0.791781, 0.377939), normColor(0.386433, 0.794644, 0.372886),
			normColor(0.395174, 0.797475, 0.367757), normColor(0.404001, 0.800275, 0.362552),
			normColor(0.412913, 0.803041, 0.357269), normColor(0.421908, 0.805774, 0.351910),
			normColor(0.430983, 0.808473, 0.346476), normColor(0.440137, 0.811138, 0.340967),
			normColor(0.449368, 0.813768, 0.335384), normColor(0.458674, 0.816363, 0.329727),
			normColor(0.468053, 0.818921, 0.323998), normColor(0.477504, 0.821444, 0.318195),
			normColor(0.487026, 0.823929, 0.312321), normColor(0.496615, 0.826376, 0.306377),
			normColor(0.506271, 0.828786, 0.300362), normColor(0.515992, 0.831158, 0.294279),
			normColor(0.525776, 0.833491, 0.288127), normColor(0.535621, 0.835785, 0.281908),
			normColor(0.545524, 0.838039, 0.275626), normColor(0.555484, 0.840254, 0.269281),
			normColor(0.565498, 0.842430, 0.262877), normColor(0.575563, 0.844566, 0.256415),
			normColor(0.585678, 0.846661, 0.249897), normColor(0.595839, 0.848717, 0.243329),
			normColor(0.606045, 0.850733, 0.236712), normColor(0.616293, 0.852709, 0.230052),
			normColor(0.626579, 0.854645, 0.223353), normColor(0.636902, 0.856542, 0.216620),
			normColor(0.647257, 0.858400, 0.209861), normColor(0.657642, 0.860219, 0.203082),
			normColor(0.668054, 0.861999, 0.196293), normColor(0.678489, 0.863742, 0.189503),
			normColor(0.688944, 0.865448, 0.182725), normColor(0.699415, 0.867117, 0.175971),
			normColor(0.709898, 0.868751, 0.169257), normColor(0.720391, 0.870350, 0.162603),
			normColor(0.730889, 0.871916, 0.156029), normColor(0.741388, 0.873449, 0.149561),
			normColor(0.751884, 0.874951, 0.143228), normColor(0.762373, 0.876424, 0.137064),
			normColor(0.772852, 0.877868, 0.131109), normColor(0.783315, 0.879285, 0.125405),
			normColor(0.793760, 0.880678, 0.120005), normColor(0.804182, 0.882046, 0.114965),
			normColor(0.814576, 0.883393, 0.110347), normColor(0.824940, 0.884720, 0.106217),
			normColor(0.835270, 0.886029, 0.102646), normColor(0.845561, 0.887322, 0.099702),
			normColor(0.855810, 0.888601, 0.097452), normColor(0.866013, 0.889868, 0.095953),
			normColor(0.876168, 0.891125, 0.095250), normColor(0.886271, 0.892374, 0.095374),
			normColor(0.896320, 0.893616, 0.096335), normColor(0.906311, 0.894855, 0.098125),
			normColor(0.916242, 0.896091, 0.100717), normColor(0.926106, 0.897330, 0.104071),
			normColor(0.935904, 0.898570, 0.108131), normColor(0.945636, 0.899815, 0.112838),
			normColor(0.955300, 0.901065, 0.118128), normColor(0.964894, 0.902323, 0.123941),
			normColor(0.974417, 0.903590, 0.130215), normColor(0.983868, 0.904867, 0.136897),
			normColor(0.993248, 0.906157, 0.143936) };

	/**
	 *
	 *
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 */
	public static int color(int v1, int v2, int v3) {
		if (v1 > 255) {
			v1 = 255;
		} else if (v1 < 0) {
			v1 = 0;
		}
		if (v2 > 255) {
			v2 = 255;
		} else if (v2 < 0) {
			v2 = 0;
		}
		if (v3 > 255) {
			v3 = 255;
		} else if (v3 < 0) {
			v3 = 0;
		}
		return 0xff000000 | v1 << 16 | v2 << 8 | v3;
	}

	public static int normColor(final double v1, final double v2, final double v3) {
		return color((int) (256 * v1), (int) (256 * v2), (int) (256 * v3));
	}

	/**
	 * Wavelength to RGB color
	 *
	 * @param wavelength
	 *            wavelength from 400nm-700nm
	 * @return
	 */
	public static int spectralColor(final double wavelength) // RGB <0,1> <-
																// lambda l
	// <400,700>
	// [nm]
	{
		double t;

		double r = 0.0;
		double g = 0.0;
		double b = 0.0;
		// RED
		if (wavelength >= 400.0 && wavelength < 410.0) {
			t = (wavelength - 400.0) / (410.0 - 400.0);
			r = +(0.33 * t) - 0.20 * t * t;
		} else if (wavelength >= 410.0 && wavelength < 475.0) {
			t = (wavelength - 410.0) / (475.0 - 410.0);
			r = 0.14 - 0.13 * t * t;
		} else if (wavelength >= 545.0 && wavelength < 595.0) {
			t = (wavelength - 545.0) / (595.0 - 545.0);
			r = +(1.98 * t) - t * t;
		} else if (wavelength >= 595.0 && wavelength < 650.0) {
			t = (wavelength - 595.0) / (650.0 - 595.0);
			r = 0.98 + 0.06 * t - 0.40 * t * t;
		} else if (wavelength >= 650.0 && wavelength < 700.0) {
			t = (wavelength - 650.0) / (700.0 - 650.0);
			r = 0.65 - 0.84 * t + 0.20 * t * t;
		}
		// GREEN
		if (wavelength >= 415.0 && wavelength < 475.0) {
			t = (wavelength - 415.0) / (475.0 - 415.0);
			g = +(0.80 * t * t);
		} else if (wavelength >= 475.0 && wavelength < 590.0) {
			t = (wavelength - 475.0) / (590.0 - 475.0);
			g = 0.8 + 0.76 * t - 0.80 * t * t;
		} else if (wavelength >= 585.0 && wavelength < 639.0) {
			t = (wavelength - 585.0) / (639.0 - 585.0);
			g = 0.84 - 0.84 * t;
		}
		// BLUE
		if (wavelength >= 400.0 && wavelength < 475.0) {
			t = (wavelength - 400.0) / (475.0 - 400.0);
			b = +(2.20 * t) - 1.50 * t * t;
		} else if (wavelength >= 475.0 && wavelength < 560.0) {
			t = (wavelength - 475.0) / (560.0 - 475.0);
			b = 0.7 - t + 0.30 * t * t;
		}
		return color((int) (256 * r), (int) (256 * g), (int) (256 * b));
	}

	static double clamp(final double x) {
		if (x > 1.0) {
			return 1.0;
		}
		if (x < 0.0) {
			return 0.0;
		}
		return x;

	}

	static WB_Vector bump3y(final WB_Vector x, final WB_Vector yoffset) {
		WB_Vector y = new WB_Vector(1.0 - x.xd() * x.xd(), 1.0 - x.yd() * x.yd(), 1.0 - x.zd() * x.zd());
		y.subSelf(yoffset);
		y.set(clamp(y.xf()), clamp(y.yf()), clamp(y.zf()));
		return y;
	}

	/**
	 *
	 * @param wavelength
	 *            400-700
	 * @return
	 */
	public static int spectralColorZucconi6(final double wavelength) {
		WB_Vector x = new WB_Vector(1.0, 1.0, 1.0).mulSelf((wavelength - 400.0) / 300.0);

		WB_Vector c1 = new WB_Vector(3.54585104, 2.93225262, 2.41593945);
		WB_Vector x1 = new WB_Vector(0.69549072, 0.49228336, 0.27699880);
		WB_Vector y1 = new WB_Vector(0.02312639, 0.15225084, 0.52607955);

		WB_Vector c2 = new WB_Vector(3.90307140, 3.21182957, 3.96587128);
		WB_Vector x2 = new WB_Vector(0.11748627, 0.86755042, 0.66077860);
		WB_Vector y2 = new WB_Vector(0.84897130, 0.88445281, 0.73949448);

		WB_Vector c1xmx1 = x.sub(x1).scaleSelf(c1.xd(), c1.yd(), c1.zd());
		WB_Vector c2xmx2 = x.sub(x2).scaleSelf(c2.xd(), c2.yd(), c2.zd());
		WB_Vector result = bump3y(c1xmx1, y1).addSelf(bump3y(c2xmx2, y2));
		return color((int) (259.99 * result.xd()), (int) (259.99 * result.yd()), (int) (259.99 * result.zd()));
	}
	// http://planetpixelemporium.com/tutorialpages/light.html

	static public int Candle = color(255, 147, 41);

	static public int Tungsten40W = color(255, 197, 143);

	static public int Tungsten100W = color(255, 214, 170);

	static public int Halogen = color(255, 241, 224);

	static public int CarbonArc = color(255, 250, 244);

	static public int HighNoonSun = color(255, 255, 251);

	static public int DirectSunlight = color(255, 255, 255);

	static public int OvercastSky = color(201, 226, 255);

	static public int ClearBlueSky = color(64, 156, 255);

	static public int WarmFluorescent = color(255, 244, 229);

	static public int StandardFluorescent = color(244, 255, 250);

	static public int CoolWhiteFluorescent = color(212, 235, 255);

	static public int FullSpectrumFluorescent = color(255, 244, 242);

	static public int GrowLightFluorescent = color(255, 239, 247);

	static public int BlackLightFluorescent = color(167, 0, 255);

	static public int MercuryVapor = color(216, 247, 255);

	static public int SodiumVapor = color(255, 209, 178);

	static public int MetalHalide = color(242, 252, 255);

	static public int HighPressureSodium = color(255, 183, 76);

}
