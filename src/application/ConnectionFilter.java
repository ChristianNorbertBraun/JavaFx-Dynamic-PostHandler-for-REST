package application;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionFilter {

	public static HashMap<String, String> getLinksFromHeader(HttpURLConnection con) {
		HashMap<String, String> returnValue = new HashMap<String, String>();
		Map<String, List<String>> headerName = con.getHeaderFields();
		List<String> links = headerName.get("Link");
		
		for(String current: links){
			String[] preparedLinks = prepareLinks(current);
			returnValue.put(preparedLinks[1].trim(), preparedLinks[0].trim());
			System.out.println(preparedLinks[1]+" "+preparedLinks[0]);
		}

		return returnValue;
	}
	
	private static String[] prepareLinks(String unpreparedLink){
		String[] linkWithRelType = unpreparedLink.split(";");
//		have to remove unnecessary characters using RegEx
		linkWithRelType[0] = linkWithRelType[0].replaceAll("[<>]", "");
		linkWithRelType[1] = linkWithRelType[1].replace("rel=", "");
		linkWithRelType[1] = linkWithRelType[1].replace("\"", "");
		return linkWithRelType;
	}

}
