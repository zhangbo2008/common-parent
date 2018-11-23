package com.uetty.common.tool.core.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.uetty.common.tool.core.HttpMultipartUtil;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Format;
import jxl.read.biff.BiffException;

/**
 * excel读取工具类
 * <p>支持范围：xls格式
 * <p>缺点：不支持xlsx格式
 * @author vince
 */
public class JexcelReader implements Closeable {
	
	Logger logger = Logger.getLogger(HttpMultipartUtil.class);
	
	/** workbook对象. */
    private Workbook wb;
    private InputStream is;
    private final Object closeLock = new Object();
    private volatile boolean closed = false;
	
    public JexcelReader(InputStream is) {
		try {
			this.is = is;
			wb = Workbook.getWorkbook(is);
		} catch (BiffException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
    public JexcelReader(File file) {
		try {
			wb = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
    public int getSheetNum() {
    	assertNoClosed();
    	return wb.getNumberOfSheets();
    }
    
    public Sheet getSheetAt(int index) {
    	assertNoClosed();
    	return wb.getSheet(index);
    }
    
    public Sheet getSheetByName(String name) {
    	assertNoClosed();
    	return wb.getSheet(name);
    }
    
    public String[] getSheetNames() {
    	assertNoClosed();
    	return wb.getSheetNames();
    }
    
    public Cell getCell(int sheetNum, int row, int col) {
    	assertNoClosed();
    	Sheet sheet = wb.getSheet(sheetNum);
    	return sheet.getCell(row, col);
    }
    
    public List<List<String>> toListList(int sheetNum) {
    	assertNoClosed();
    	Sheet sheet = wb.getSheet(sheetNum);
    	List<List<String>> listList = new ArrayList<List<String>>();
    	for (int i = 0; i < sheet.getRows(); i++) {
    		Cell[] row = sheet.getRow(i);
    		List<String> list = new ArrayList<String>();
    		for (int j = 0; j < row.length; j++) {
    			list.add(row[j].getContents());
    			CellFeatures cellFeatures = row[j].getCellFeatures();
    			if (cellFeatures != null) {
    				@SuppressWarnings("unused")
    				String comment = cellFeatures.getComment();
    			}
    			CellFormat cellFormat = row[j].getCellFormat();
    			if (cellFormat != null) {
    				Format format = cellFormat.getFormat();
    				@SuppressWarnings("unused")
    				String formatString = format.getFormatString();
    				
    			}
    			@SuppressWarnings("unused")
    			int columnCnt = row[j].getColumn();
    			@SuppressWarnings("unused")
    			int rowCnt = row[j].getRow();
    		}
    		listList.add(list);
    	}
    	return listList;
    }
    
    public List<String> getRowValue(int sheetNum, int rowNum) {
    	assertNoClosed();
    	Sheet sheet = wb.getSheet(sheetNum);
    	List<String> list = new ArrayList<String>();
    	Cell[] row = sheet.getRow(rowNum);
    	for (int i = 0; i < row.length; i++) {
    		String contents = row[i].getContents();
    		list.add(contents);
    	}
    	return list;
    }
    
    private void assertNoClosed() {
    	if (closed) {
    		throw new RuntimeException("cannot operate after closed");
    	}
    }
    
    @Override
    public void close() throws IOException {
    	synchronized (closeLock) {
    		if (closed) return;
    		closed = true;
		}
    	if (is != null) {
    		try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	is = null;
    	if (wb != null) {
    		try {
    			wb.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	wb = null;
    }
    
    @Override
    protected void finalize() throws Throwable {
    	close();
    }
    
	public boolean isClosed() {
		return closed;
	}

	/**
     * 读取表格
     */
    public List<String> readExcel(InputStream is) {

        Workbook wb = null;

        try {

            List<String> retList = new ArrayList<String>();
            wb = Workbook.getWorkbook(is);
            Sheet sheet = wb.getSheet(0); // 默认取0页

            for (int i = 1; i < sheet.getRows(); i++) {

                String phone = sheet.getCell(0, i).getContents();

                if (StringUtils.isEmpty(phone)) {
                    logger.debug("skip read biz data,sn is emtpy.");
                    continue;
                }

                phone = phone.replace(" ", "");

                retList.add(phone);
            }

            return retList;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (wb != null) {
                wb.close();
            }
        }

        return null;
    }
}
