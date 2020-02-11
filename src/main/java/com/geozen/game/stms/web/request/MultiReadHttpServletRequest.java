package com.geozen.game.stms.web.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
	private ByteArrayOutputStream cachedBytes;

	public MultiReadHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (cachedBytes == null)
			cacheInputStream();

		return new CachedServletInputStream();
	}

	private void cacheInputStream() throws IOException {
		/*
		 * Cache the inputstream in order to read it multiple times. For convenience, I
		 * use apache.commons IOUtils
		 */
		cachedBytes = new ByteArrayOutputStream();
		IOUtils.copy(super.getInputStream(), cachedBytes);
	}

	public byte[] getBodyByteArray() throws IOException {
		getInputStream();
		return cachedBytes != null ? cachedBytes.toByteArray() : null;
	}

	/* An inputstream which reads the cached request body */
	public class CachedServletInputStream extends ServletInputStream {
		private ByteArrayInputStream input;

		public CachedServletInputStream() {
			/* create a new input stream from the cached request body */
			input = new ByteArrayInputStream(cachedBytes.toByteArray());
		}

		@Override
		public int read() throws IOException {
			return input.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {

		}
	}
}