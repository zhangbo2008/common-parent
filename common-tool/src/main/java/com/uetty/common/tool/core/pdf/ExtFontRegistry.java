package com.uetty.common.tool.core.pdf;

import com.itextpdf.text.FontFactory;

import fr.opensagres.xdocreport.itext.extension.font.AbstractFontRegistry;

public class ExtFontRegistry extends AbstractFontRegistry {

	public static ExtFontFactoryImpl extFontFactoryImp = new ExtFontFactoryImpl();
	private static final ExtFontRegistry INSTANCE = new ExtFontRegistry();
	
	private ExtFontRegistry() {
		FontFactory.setFontImp(extFontFactoryImp);
	}

	@Override
	protected String resolveFamilyName(String familyName, int style) {
		if ("宋体".equals(familyName) || "SimSun".equals(familyName)) {
			return "simsun";
		}
		if ("微软雅黑".equals(familyName)) {
			return "microsoft yahei";
		}
		return "microsoft yahei";
	}

	public static ExtFontRegistry getRegistry() {
		return INSTANCE;
	}
}