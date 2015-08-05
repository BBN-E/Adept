/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

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