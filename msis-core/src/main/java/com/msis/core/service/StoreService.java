package com.msis.core.service;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Drug;
import com.msis.core.model.Store;

public interface StoreService {
	Drug createDrug(Drug drug) throws ServiceException;
	Drug updateDrug(Drug drug) throws ServiceException;
	void deleteDrug(String id) throws ServiceException;
	
	Store createStore(Store store) throws ServiceException;
	Store updateStore(Store store) throws ServiceException;
	void deleteStore(String id) throws ServiceException;
	
	int inStock(String drugId, Long dateTime);
	int checkInStock(int number, String drugId, Long dateTime) throws ServiceException;
}
