package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Drug;
import com.msis.core.model.Store;

public interface StoreService {
	Drug createDrug(Drug drug, String accessToken) throws ServiceException;
	Drug updateDrug(Drug drug, String accessToken) throws ServiceException;
	void deleteDrug(String id, String accessToken) throws ServiceException;
	List<Drug> findDrugByCreator(String creator, String accessToken) throws ServiceException;
	
	Store createStore(Store store) throws ServiceException;
	Store updateStore(Store store) throws ServiceException;
	void deleteStore(String id) throws ServiceException;
	
	int inStock(String drugId, Long dateTime);
	int checkInStock(int number, String drugId, Long dateTime) throws ServiceException;
}
