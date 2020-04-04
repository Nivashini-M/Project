package com.codinground.Utils;

//$Id$


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;*/
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtils extends Utility {

	public HSSFWorkbook wb;
	
	public HSSFSheet sheet;
	public HSSFRow row;
	public HSSFCell cell;
	static DataFormatter formatter = new DataFormatter();
	static String fileName;
	static InputStream input;

	/**
	 * @Desc Sets the Sheet Configuration for POI
	 * @throws IOException
	 */
	public void setSheet(String sheetName) throws IOException {
		fileName = new File("./src/test/resources/testResources/" + sheetName + ".xls").getAbsolutePath();

		input = new FileInputStream(fileName);
		wb = new HSSFWorkbook(input);
		sheet = wb.getSheetAt(0);
	}

	public void clearData() throws IOException {
		for (int index = sheet.getLastRowNum(); index > sheet.getFirstRowNum(); index--) {

			if (sheet.getRow(index) != null) {
				sheet.removeRow(sheet.getRow(index));
			}
		}
		FileOutputStream out = null;
		try {

			out = new FileOutputStream(new File(fileName));
			wb.write(out);
		} finally {
			out.close();
		}
	}

	public void setValue(HashMap<String, HashMap<String, String>> postDetails) throws IOException {
		Set<String> values = postDetails.keySet();
		int rowNumber = sheet.getLastRowNum() + 1;
		for (String key : values) {
			HashMap<String, String> valueToInsert = null;
			if (postDetails.get(key) == null) {
				HashMap<String, String> nullval = new HashMap<String, String>();
				nullval.put("null", "null");
				postDetails.put(key, nullval);
				valueToInsert = postDetails.get(key);
			} else {
				valueToInsert = postDetails.get(key);
			}
			updateDataintoCell(rowNumber, key, valueToInsert);
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(fileName));
			wb.write(out);
		} finally {
			out.close();
		}
	}

	public ArrayList<HashMap<String, HashMap<String, String>>> getValue() throws IOException, ClassNotFoundException {
		HashMap<String, HashMap<String, String>> valuesToBeRetrieved = getHeaders();
		
		HashMap<String, HashMap<String, String>> newmap = new HashMap<String, HashMap<String, String>>();
		ArrayList<HashMap<String, HashMap<String, String>>> listOfPostDetails = new ArrayList<HashMap<String, HashMap<String, String>>>();
		List<HashMap<String, HashMap<String,String>>> coll = new ArrayList<HashMap<String, HashMap<String,String>>>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			
			HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
//			map=valuesToBeRetrieved;
			Set<String> keys = valuesToBeRetrieved.keySet();
			ArrayList<String> valuesToBeRemoved = new ArrayList<String>();

			for (String key : keys) {
				String streamValue = getCellLatestValue(i, key);
				if (!streamValue.contains("EmptyValue")) {
					map.put(key, (HashMap<String, String>) deserialize(getCellLatestValue(i, key)));
				} else {
					valuesToBeRemoved.add(key);

				}
			}
			for (String a : valuesToBeRemoved) {
				map.remove(a);
			}		
			listOfPostDetails.add(map);
		}
	
		
		return listOfPostDetails;
	}
	public HashMap<String, HashMap<String, String>> getValue(int i) throws IOException, ClassNotFoundException {
		HashMap<String, HashMap<String, String>> valuesToBeRetrieved = getHeaders();
		
		
		
		
			
			HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
			map=valuesToBeRetrieved;
			Set<String> keys = map.keySet();
			ArrayList<String> valuesToBeRemoved = new ArrayList<String>();

			for (String key : keys) {
				String streamValue = getCellLatestValue(i, key);
				if (!streamValue.contains("EmptyValue")) {
					map.put(key, (HashMap<String, String>) deserialize(getCellLatestValue(i, key)));
				} else {
					valuesToBeRemoved.add(key);

				}
			}
			for (String a : valuesToBeRemoved) {
				map.remove(a);
			}		
		
		
		return map;
	}

	/*public void updateDataintoCell(int rowNumber, String key, HashMap<String, String> value) throws IOException {

		if (sheet.getRow(rowNumber) == null) {
			row = sheet.createRow(rowNumber);
		} else {
			row = sheet.getRow(rowNumber);
		}
		cell = row.createCell(getCellNumber(key));
		cell.setCellValue(serialize(value));
//		cell.setCellValue(value.toString());
	}

	public String getCellLatestValue(int rowNumber, String key) {

		String valueToExtract = "";
		if (sheet.getRow(rowNumber).getCell(getCellNumber(key)) == null) {
			valueToExtract = "EmptyValue";
		} else {
			valueToExtract = sheet.getRow(rowNumber).getCell(getCellNumber(key)).getStringCellValue();
		}

		return valueToExtract;
	}

	private static String serialize(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		try {

			oos.writeObject(o);

		} finally {
			oos.close();
			baos.close();

		}
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	private static Object deserialize(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		try {

			ois.close();
		} finally {
			ois.close();
		}
		return o;

	}

	public int getCellNumber(String value) {
		int valueToReturn = 0;
		for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
			if (sheet.getRow(0).getCell(j).getStringCellValue().equalsIgnoreCase(value)) {
				valueToReturn = j;
				break;
			}
		}
		return valueToReturn;
	}

	public HashMap<String, HashMap<String, String>> getHeaders() {
		HashMap<String, HashMap<String, String>> retrieve = new HashMap<String, HashMap<String, String>>();
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
			HashMap<String, String> empty = new HashMap<String, String>();
			retrieve.put(sheet.getRow(0).getCell(i).getStringCellValue(), empty);
		}

		return retrieve;
	}

	/*public void updateDate() throws IOException {
		FileOutputStream out = null;
		try {
			int maxRows = sheet.getLastRowNum();
			for (int i = 0; i <= maxRows; i++) {
				sheet.getRow(i).getCell(0).setCellValue(getDateAfterAddingMinute(20, "MM/dd/yyyy HH:mm"));
			}
			out = new FileOutputStream(new File(fileName));
			wb.write(out);
		} finally {
			out.close();
		}
	}

	/**
	 * @Desc Returns Data matching keyword given
	 * @param value
	 * @return String value Matching "value" from Sheet
	 */
	public String getData(String value) {
		List<Integer> pair = getRefValues(sheet, value);
		row = sheet.getRow(pair.get(0));
		return row.getCell(pair.get(1) + 1).getStringCellValue();
	}

	/**
	 * @Desc Returns filePath matching keyword given
	 * @param value
	 * @return filePath value Matching "value" from Sheet
	 */
	/*public String getDataImg(String value) {
		return new File(testResourcesPath + getData(value)).getAbsolutePath();
	}

	/**
	 * @param value
	 * @return testData from Excel
	 */
	public String getDataTxt(String value) {
		return getData(value);
	}

	/**
	 * @param value
	 * @return Random Image from Excel
	 * @throws IOException
	 */
	/*public String getDataImgs(String value) throws IOException {
		List<String> result = getDatas(value);

		return new File(testResourcesPath + result.get(randomNumber(0, result.size() - 1))).getAbsolutePath();
	}

	/**
	 * @param value
	 * @return random Text from Excel
	 * @throws IOException
	 */
	/*public String getDataTxts(String value) throws IOException {
		List<String> result = getDatas(value);
		return result.get(randomNumber(0, result.size() - 1));
//		return result.get(0);

	}
	
	/**
	 * @return processed Randomized String
	 * @throws IOException 
	 */
	/*public String getProcessedText() throws IOException
	{
		List<String> result = getDatas("Text Contents");
		
	
		String data= result.get(randomNumber(0, result.size() - 1));
	
		
		data=processText(data);
	
		
		
		return data;
	}
	
	/**
	 * @return processed Randomized String
	 * @throws IOException 
	 */
	public String getProcessedLinkText() throws IOException
	{
		List<String> result = getDatas("Text Contents For Link");
		
	
		String data= result.get(randomNumber(0, result.size() - 1));
	
		
		data=processText(data);
	
		
		
		return data;
	}

	
	/**
	 * @Desc return Processed Text
	 * @param text
	 */
	/*public String processText(String text)
	{
		String day=getDateinFormat("d");
		String[] hour= {"ä¸€YÄ«","dos","three","à¤šà¤¾à¤°","Ø®Ù…Ø³Ø©","seis","à¦¸à¦¾à¦¤","Ð²Ð¾ÑÐµÐ¼ÑŒ","ä¹","sepuluh","elf","ì—´ ë‘ë²ˆì§¸","treize","à°ªà°¦à±à°¨à°¾à°²à±à°—à±","onbeÅŸ","à®ªà®¤à®¿à®©à®¾à®±à¯","mÆ°á»i báº£y","Ø§Ù¹Ú¾Ø§Ø±Û","dix-neuf","zwanzig","veintiuno","vinte e dois","äºŒåä¸‰","äºŒåå…­"};


		int hourChoosen=Integer.parseInt(getDateinFormat("H"));
		String sec=getDateinFormat("ss");
		String hourToProcess=hour[hourChoosen];
		
		
		String min=getDateinFormat("mm");
		
		String[] splChars= {"~","`","!","@","#","$","%","&","*","(",")","_","-","+","=","{","}","[","]","|","\\",";",":","\"","'","<",">",",",".","?","/"};
		splChars=getShuffledOrder(splChars);
		int position=text.length()/splChars.length;
		text=insertValuesAtRandomPositions(text, splChars, position);
		
		String[] values= {"minValue","hourValue","dayValue","specialLetter","tamilLanguage","arabicLanguage","emojiIcons","hashTagValue","mentionValue"};
		text=insertValuesAtRandomPositions(text, values, text.length()/values.length);
	
		
		text=text.replace("minValue", min);
		text=text.replace("hourValue", getDateinFormat("H"));
		text=text.replace("dayValue", day);
		String[] specialLetter= {"â„ ","â„¢","Â®","ã‹"};
		String[] tamilLanguage= {"à¯°","à¯±","à¯²","à¯³","à¯´","à¯µ","à¯¶","à¯·","à¯¸","à¯¹","à¯º","à¯¦","à¯§","à¯¨","à¯©","à¯ª","à¯«","à¯¬","à¯­","à¯®","à¯¯"," à¯†","à¯‡","à¯ˆ", "à®‚","à®ƒ","à®…","à®†","à®‡","à®ˆ","à®‰","à®Š","à®Ž","à®","à®™","à®š","à®œ" ,"à®ž","à®Ÿ","à®","à®’","à®“","à®”","à®•","à®™","à®š","à®œ","à®ž","à®Ÿ","à®£","à®¤","à®¨","à®©","à®ª","à®®","à®¯","à®°","à®±","à®²","à®³","à®´","à®µ","à®¶","à®·","à®¸","à®¹","à¯"};
		String[] arabicLanguage= {"Ú ","Ú¡","Ú²","Ù‚","Ø¶","ÚŸ","Ú¯","Ú¿","Ûž","ØŸ","Û©"};
		String[] emojiIcons= {"ðŸ˜€","ðŸ˜","ðŸ˜‚","ðŸ˜ƒ","ðŸ˜„","ðŸ˜…","ðŸ˜†","ðŸ˜‡","ðŸ˜ˆ","ðŸ˜‰","ðŸ˜Š","ðŸ˜‹","ðŸ˜Œ","ðŸ˜","ðŸ˜Ž","ðŸ˜","ðŸ˜","ðŸ˜‘","ðŸ˜’","ðŸ˜“","ðŸ˜”","ðŸ˜•","ðŸ˜–","ðŸ˜—","ðŸ˜˜","ðŸ˜™","ðŸ˜š","ðŸ˜›","ðŸ˜œ","ðŸ˜","ðŸ˜ž","ðŸ˜Ÿ","ðŸ˜ ","ðŸ˜¡","ðŸ˜¢","ðŸ˜£","ðŸ˜¤","ðŸ˜¥","ðŸ˜¦","ðŸ˜§","ðŸ˜¨","ðŸ˜©","ðŸ˜ª","ðŸ˜«","ðŸ˜¬","ðŸ˜­","ðŸ˜®","ðŸ˜¯","ðŸ˜°","ðŸ˜±","ðŸ˜²","ðŸ˜³","ðŸ˜´","ðŸ˜µ","ðŸ˜¶","ðŸ˜·","ðŸ˜¸","ðŸ˜¹","ðŸ˜º","ðŸ˜»","ðŸ˜¼","ðŸ˜½","ðŸ˜¾","ðŸ˜¿","ðŸ™€","ðŸ™‚","ðŸ™ƒ","ðŸ™„","ðŸ™…","ðŸ™†","ðŸ™‡","ðŸ™ˆ","ðŸ™‰","ðŸ™Š","ðŸ™‹","ðŸ™Œ","ðŸ™","ðŸ™Ž","ðŸ™"};
		text=text.replace("emojiIcons", emojiIcons[randomNumber(0, emojiIcons.length-1)]);
		text=text.replace("specialLetter", specialLetter[randomNumber(0, specialLetter.length-1)]);
		text=text.replace("tamilLanguage", tamilLanguage[randomNumber(0, tamilLanguage.length-1)]);
		text=text.replace("arabicLanguage", arabicLanguage[randomNumber(0, arabicLanguage.length-1)]);
		
		String[] hashTag= {"#trend","#social","#media","#testing","#ignore","#crazy","#GoForward","#cricket"};
		String[] mention= {"@cric-bot","#@trend","@repost","@cricket","@batting","@technique","@bolt_world","@cricfan"};
		text=text.replace("hashTagValue", hashTag[randomNumber(0, hashTag.length-1)]);
		text=text.replace("mentionValue", mention[randomNumber(0, mention.length-1)]);
	
		
		return text;
	}
	
	
	/**
	 * @Desc insert values at random position
	 * @param text
	 * @param array
	 */
	public String insertValuesAtRandomPositions(String text,String[] array,int position)
	{
		int textLength=text.length();
//		textLength=textLength-randomNumber(1, 3);
		for(int i=0;i<array.length;i++)
		{
			StringBuffer str =  new StringBuffer(text);
			str.insert(textLength, array[i]);
			textLength=textLength-position;
			text=str.toString();
		}
		return text;
	}
	
	
	/**
	 * @return shuffled order of input array
	 * @param array
	 */
	public String[] getShuffledOrder(String[] array)
	{
		  Random r = new Random(); 
        int n=array.length;
	        // Start from the last element and swap one by one. We don't 
	        // need to run for the first element that's why i > 0 
	        for (int i = n-1; i > 0; i--) { 
	              
	            // Pick a random index from 0 to i 
	            int j = r.nextInt(i+1); 
	              
	            // Swap arr[i] with the element at random index 
	            String temp = array[i]; 
	            array[i] = array[j]; 
	            array[j] = temp; 
	        } 
	        return array;
	}
	public String[] getDataTxtsArray(String value) throws IOException {
		List<String> result = getDatas(value);
		String[] array = new String[result.size()];
		return result.toArray(array);
//		return result.get(8);
	}

	/**
	 * @param value
	 * @return values Matching Keyword in Excel
	 * @throws IOException
	 */
	public List<String> getDatas(String value) throws IOException {
		List<Integer> pair = getRefValues(sheet, value);
		List<String> result = new ArrayList<String>();
		row = sheet.getRow(pair.get(0));
		int maxRows = row.getLastCellNum();
		for (int i = 1; i < maxRows; i++) {
			
			result.add(row.getCell(i).getStringCellValue());
		}
		// input.close();
		return result;
	}

	/**
	 * @param sheet
	 * @param cellContent
	 * @return row & cell of the matching Content from Excel
	 */
	private static List<Integer> getRefValues(HSSFSheet sheet, String cellContent) {
		List<Integer> ref = new ArrayList<Integer>();
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (formatter.formatCellValue(cell).equalsIgnoreCase(cellContent)) {
						ref.add(row.getRowNum());
						ref.add(cell.getColumnIndex());
						return ref;
					}
				}
			}
		}
		return ref;
	}

	// Write in Excel
	public void fillData(HashMap<String, String> data) throws IOException {
		FileOutputStream out = null;
//		row = sheet.getRow(sheet.getLastRowNum());
		try {
			row = sheet.createRow(sheet.getLastRowNum() + 1);

			int cellcount = 0;
			List<String> keys = new ArrayList<String>();
			for (String key : data.keySet()) {
				keys.add(key);
			}
			for (int i = 0; i < data.size(); i++) {
				cell = row.createCell(cellcount);

				cell.setCellValue(data.get(keys.get(i)));
				cellcount++;
			}
			out = new FileOutputStream(new File(fileName));
			wb.write(out);
		} finally {
			out.close();
		}
	}
}


