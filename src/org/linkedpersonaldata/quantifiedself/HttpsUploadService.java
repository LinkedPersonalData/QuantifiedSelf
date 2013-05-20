package org.linkedpersonaldata.quantifiedself;

import edu.mit.media.funf.storage.HttpUploadService;
import edu.mit.media.funf.storage.RemoteFileArchive;

public class HttpsUploadService extends HttpUploadService {

	@Override
	protected RemoteFileArchive getRemoteArchive(String name) {
		PDSWrapper pds = null;
		try {
			pds = new PDSWrapper(this);
		} catch (Exception e) {
		}
		
		return new HttpsArchive(pds);
	}

}
