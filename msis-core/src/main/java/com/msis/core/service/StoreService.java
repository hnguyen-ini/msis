package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Drug;
import com.msis.core.model.Store;

public interface StoreService {
	Drug saveDrug(Drug drug, String accessToken) throws ServiceException;
	void deleteDrug(String id, String accessToken) throws ServiceException;
	List<Drug> findDrugByCreator(String creator, String accessToken) throws ServiceException;
	List<Drug> searchDrugByCreatorAndNameLike(String creator, String name, String accessToken) throws ServiceException;
	
	Drug createStore(Store store, String accessToken) throws ServiceException;
	Store updateStore(Store store) throws ServiceException;
	void deleteStore(String id) throws ServiceException;
	List<Store> getStoreByDrug(String drugId, String accessToken) throws ServiceException;
	
	int inStock(String drugId, Long dateTime);
	int checkInStock(int number, String drugId, Long dateTime) throws ServiceException;
}
