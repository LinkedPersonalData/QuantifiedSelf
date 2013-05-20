package org.linkedpersonaldata.quantifiedself;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;
import edu.mit.media.funf.storage.HttpArchive;
import edu.mit.media.funf.storage.RemoteFileArchive;

public class HttpsArchive implements RemoteFileArchive {

	private PDSWrapper mPds;

	public HttpsArchive(PDSWrapper pds) {
		mPds = pds;
	}

	public String getId() {
		return mPds.buildAbsoluteUrl("");
	}

	public boolean add(File file) {
		if (mPds == null) {
			return false;
		}
		return mPds.uploadFunfData(file);
	}
}
