package com.uetty.common.tool.core.pdf;

import java.net.URL;

import com.lowagie.text.FontFactoryImp;

class ExtFontFactoryImpl extends FontFactoryImp {

	public ExtFontFactoryImpl() {
		super();
	}

	public int registerDirectories() {
		int i = 0;
		
        URL resource = this.getClass().getResource("pdf/fonts");
        String fontFolder = resource.getPath();
        
		i += registerDirectory(fontFolder, true);
		i += registerDirectory("/usr/share/fonts", true);
		return i;
	}
}
