package com.msis.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.common.utils.ListUtils;
import com.msis.core.cache.CacheService;
import com.msis.core.model.Drug;
import com.msis.core.model.Patient;
import com.msis.core.model.Store;
import com.msis.core.repository.DrugRepository;
import com.msis.core.repository.StoreRepository;
import com.msis.core.service.StoreService;

@Service(value="storeService")
public class StoreServiceImpl implements StoreService {
	static Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);
	
	@Autowired
	private DrugRepository drugRepo;
	@Autowired
	private StoreRepository storeRepo;
	@Autowired
	private CacheService cacheService;
	
	/**
	 * DRUG ZONE
	 */
	@Override
	public Drug saveDrug(Drug drug, String accessToken) throws ServiceException {
		try {
			if (drug.getName() == null || drug.getName().isEmpty()
					|| drug.getCreator() == null || drug.getCreator().isEmpty()) {
				log.warn("Create Drug:: Missing name or creator");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing name or creator");
			}
			cacheService.checkAccessToken(accessToken);
			
			Drug d = drugRepo.findByNameAndCreator(drug.getName(), drug.getCreator());
			if (d != null) {
				d.setDescription(drug.getDescription());
			} else {
				d = new Drug(drug.getName(), drug.getDescription(), drug.getCreator());
			}
			drugRepo.save(d);
			return d;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Create Drug:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	@Override
	public void deleteDrug(String id, String accessToken) throws ServiceException {
		try {
			if (id == null || id.isEmpty()) {
				log.warn("Delete Drug:: Missing id");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing id");
			}
			cacheService.checkAccessToken(accessToken);
			Drug del = drugRepo.findOne(id);
			if (del == null) {
				log.warn("Delete Drug:: Not found drug by " + id);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found drug by " + id);
			}
			drugRepo.delete(del);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Drug:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public List<Drug> findDrugByCreator(String creator, String accessToken) throws ServiceException {
		try {
			cacheService.checkAccessToken(accessToken);
			return ListUtils.okList(drugRepo.findByCreator(creator));
		} catch (ServiceException e) {
			throw e;
		} catch(Exception e) {
			log.warn("Find Drug by Creator:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public List<Drug> searchDrugByCreatorAndNameLike(String creator, String name, String accessToken) throws ServiceException {
		try {
			cacheService.checkAccessToken(accessToken);
			List<Drug> drugs = new ArrayList<Drug>();
			for (String n : name.split(" ")) {
				List<Drug> list = drugRepo.findByNameLikeAndCreator(n, creator);
				if (list != null) {
					for (Drug d : list) {
						boolean exist = false;
						for (Drug p1 : drugs) {
							if (d.getId().equals(p1.getId())) {
								exist = true;
								break;
							}
						}
						if (!exist)
							drugs.add(d);
					}
				}
			}
			return drugs;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Find Drug by Creator:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	
	/**
	 * STORE ZONE
	 */
	@Override
	public Store createStore(Store store) throws ServiceException {
		try {
			if (store.getDrugId() == null || store.getDrugId().isEmpty()
					|| store.getNumber() == null || store.getNumber() == 0) {
				log.warn("Create Store:: Missing drugId or number");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing drugId or number");
			}
			if (checkInStock(store.getNumber(), store.getDrugId(), System.currentTimeMillis()) < 0) {
				log.warn("Create Store:: Over of instock");
				throw new ServiceException(ServiceStatus.OVER_INSTOCK, "Over inStock");
			}
			store.setCreateAt(System.currentTimeMillis());
			storeRepo.save(store);
			return store;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Create Store:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	@Override
	public Store updateStore(Store store) throws ServiceException {
		try {
			if (store.getId() == null || store.getId().isEmpty() 
					|| store.getDrugId() == null || store.getDrugId().isEmpty()
					|| store.getNumber() == null || store.getNumber() == 0) {
				log.warn("Update Store:: Missing id or drugId or number");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing id or drugId or number");
			}
			Store edit = storeRepo.findOne(store.getId());
			if (edit == null) {
				log.warn("Update Store:: Not found drug by " + store.getId());
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found store by " + store.getId());
			}
			if (checkInStock(store.getNumber(), edit.getDrugId(), edit.getCreateAt()) < 0) {
				log.warn("Update Store:: Over of instock");
				throw new ServiceException(ServiceStatus.OVER_INSTOCK, "Over inStock");
			}
			edit.setDrugId(store.getDrugId());
			edit.setNumber(store.getNumber());
			storeRepo.save(edit);
			return edit;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Update Store:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	@Override
	public void deleteStore(String id) throws ServiceException {
		try {
			if (id == null || id.isEmpty()) {
				log.warn("Delete Store:: Missing id");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing id");
			}
			Store del = storeRepo.findOne(id);
			if (del == null) {
				log.warn("Delete Store:: Not found store by " + id);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found store by " + id);
			}
			storeRepo.delete(del);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Store:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
		
	}
	
	@Override
	public int inStock(String drugId, Long dateTime) {
		List<Store> stores = storeRepo.findByDrugIdAndCreateAtLessThan(drugId, dateTime);
		int count = 0;
		for (Store store : stores) {
			count += store.getNumber();
		}
		return count;
	}
	
	@Override
	public int checkInStock(int number, String drugId, Long dateTime) throws ServiceException {
		int count = inStock(drugId, dateTime);
		count += number;
		return count;
	}
}
