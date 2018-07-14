package com.uetty.common.tool.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.uetty.common.tool.core.pdf.ExtFontRegistry;

import freemarker.template.TemplateException;

/**
 * 文档工具
 * @author vince
 * <p> 根据模板生成docx、docx转pdf、docx加密等
 */
public class DocTool {

	/**
	 * 生成docx文件
	 * <p> 技术原理依据：docx文件本身是zip格式打包的，将zip包内部文件制作为模板，填充数据后重新打包就能生成docx文件
	 * 
	 * @param docxFile docx文件（作为样式模板）
	 * @param ftlFileMap key(String): zip包内部相对根目录的路径， value(File): 制作的ftl文件
	 * @param dataMap 替换ftl模板标签的数据
	 */
	public static File createDocx(File docxFile, Map<String, File> ftlFileMap, 
			Map<String, Object> dataMap) throws ZipException, IOException, TemplateException {
		
		Map<String, File> entryFileMap = new HashMap<String, File>();
		File outFile = null;
		ZipFile zipFile = null;
		ZipOutputStream zipout = null;
		
		try {
			// freemarker处理模板文件，生成临时文件
			for (String key : ftlFileMap.keySet()) {
				File ftlFile = ftlFileMap.get(key);
				String entryFilePath = FileTool.randomFilePathByExtName(null);
				File entryFile = FreemarkerEngine.process(dataMap, ftlFile.getAbsolutePath(), entryFilePath);
				entryFileMap.put(key, entryFile);
			}
		
			// 将临时文件替换到docx文件下相应路径
			outFile = new File(FileTool.randomFilePathByExtName("docx"));			
			
			zipFile = new ZipFile(docxFile);
			zipout = new ZipOutputStream(new FileOutputStream(outFile));
			

			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				ZipEntry next = zipEntrys.nextElement();
				InputStream is = null;
				// 生成的zip包中添加文件项
				zipout.putNextEntry(new ZipEntry(next.toString()));
				// 尝试从生成的文件读取流
				File entryFile = entryFileMap.get(next.toString());
				if (entryFile != null) {
					is = new FileInputStream(entryFile);
				}
				// 从zip包中文件读取流
				if (is == null) {
					is = zipFile.getInputStream(next);
				}
				FileTool.writeFromInputStream(zipout, is);
			}
		} finally {
			for (File entryFile : entryFileMap.values()) {
				entryFile.delete();
			}
			if (zipFile != null) {
				zipFile.close();
			}
			if (zipout != null) {
				zipout.close();
			}
		}
		return outFile;
	}

	/**
	 * docx文件转pdf文件
	 */
	public static void docxConvertToPdf(File docxFile, File outFile) throws XWPFConverterException, IOException {
		InputStream source = new FileInputStream(docxFile);
		OutputStream target = new FileOutputStream(outFile);

		XWPFDocument doc = new XWPFDocument(source);
		// 输出设置
		PdfOptions options = PdfOptions.create();
		// 字体提供者
		ExtFontRegistry fontProvider = ExtFontRegistry.getRegistry();
		options.fontProvider(fontProvider);
		
		PdfConverter.getInstance().convert(doc, target, options);
	}
	
	/**
	 * 给docx文件添加open密码
	 */
	public static void docxAddPassword(File docxFile, File outFile, String password) throws InvalidFormatException, IOException, GeneralSecurityException {
		// 加密算法与加密密钥
		EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
		Encryptor enc = info.getEncryptor();
		enc.confirmPassword(password);
		
		POIFSFileSystem fs = new POIFSFileSystem();
		// 获取输入到fs的输出流，并使用加密算法装饰输出流
		OutputStream os = enc.getDataStream(fs);
		
		// 读取docx文件，通过流输出到fs中
		OPCPackage opc = null;
		try {
			opc = OPCPackage.open(docxFile, PackageAccess.READ_WRITE);
			opc.save(os);
		} finally {
			if (opc != null) {
				opc.close();
			}
		}

		// 将fs中的数据写入到输出文件
		FileOutputStream fos = new FileOutputStream(outFile);
		fs.writeFilesystem(fos);
		fos.close();
	}
	
}
