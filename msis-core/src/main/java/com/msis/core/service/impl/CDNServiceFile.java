package com.msis.core.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.core.service.CDNService;

@Service
public class CDNServiceFile implements CDNService {
	private static Logger log = LoggerFactory.getLogger(CDNServiceFile.class);
	private static String cdnRoot = "/var/www/cdn/msis";
	
	@Override
	public long saveContent(InputStream is, String uri) throws ServiceException {
		uri = cdnRoot + uri;
		log.info("CDNService :: saveContent :: fully Qualified Uri{} " + uri);
	       
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            File file = new File(uri);
            file.getParentFile().mkdirs();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bis = new BufferedInputStream(is);
            byte[] bytez = new byte[512];
            int readCount = -1;
            while ((readCount = bis.read(bytez)) != -1) {
                bos.write(bytez, 0, readCount);
                md.update(bytez, 0, readCount);
            }
            bos.flush();
            Hex.encodeHexString(md.digest());
            return file.length();
        } catch (Exception e) {
            throw new ServiceException(ServiceStatus.NO_CONTENT, "Saving Content Failed, " + e.getMessage());
        }
        finally {
            if (bis!=null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.debug("exception in closing input stream");
                }
            }
            if (bos!=null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.debug("exception in closing output stream");
                }
            }
            if (is != null) {
            	try {
					is.close();
				} catch (IOException e) {
					log.debug("exception in closing input stream");
				}
            }
        }
	}

	@Override
	public InputStream readContent(String uri) throws ServiceException {
		try {
			uri = cdnRoot + uri;
			log.info("CDNService :: readContent :: fully Qualified Uri{} " + uri);
            return (InputStream)(new FileInputStream(uri));
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.NO_CONTENT, "Reading Content Failed, " + e.getMessage());
        }
	}

	@Override
	public void deleteContent(String uri) throws ServiceException {
		try {
			uri = cdnRoot + uri;
			File file = new File(uri);
            if (file.exists() && file.canWrite()) {
                file.delete();
            }
        } catch (Exception e) {
            log.debug("Error removing {}: {}",uri,e.getMessage());
        }
	}

	@Override
	public void cleanDirectory(File directory) throws ServiceException {
		try {
			FileUtils.cleanDirectory(directory);
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.NO_CONTENT, e.getMessage());
		}
	}

}
