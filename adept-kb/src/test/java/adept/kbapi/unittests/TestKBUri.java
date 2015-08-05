package adept.kbapi.unittests;

import adept.common.KBID;

public class TestKBUri
{
	private static KBID kbEntityUri;
	private static KBID kbRelationUri;
	
	public static void setKBEntityUri(KBID kbid)
	{
		kbEntityUri = kbid;
	}
	
	public static KBID getKBEntityUri()
	{
		return kbEntityUri;
	}
	
	public static void setKBRelationUri(KBID kbid)
	{
		kbRelationUri = kbid;
	}
	
	public static KBID getKBRelationUri()
	{
		return kbRelationUri;
	}
}