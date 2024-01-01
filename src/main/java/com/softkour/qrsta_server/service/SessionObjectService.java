package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.SessionObject;
import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;
import com.softkour.qrsta_server.repo.SessionObjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class SessionObjectService {
    @Autowired
    SessionObjectRepo sessionObjectRepo;


    public SessionObject save(String title, SessionObjectType type,Long parentId) throws ChangeSetPersister.NotFoundException {
        SessionObject newItem=new SessionObject();
        newItem.setTitle(title);
        newItem.setType(type);
        ///add sub object
        if(parentId!=null){
            SessionObject sessionObject=sessionObjectRepo.findById(parentId).orElseThrow(()->new NotFoundException("object id not found:".concat(parentId.toString())));
            sessionObject.addSubItem(newItem);
            return sessionObjectRepo.save(sessionObject);
        }
        // add new object
        return  sessionObjectRepo.save(newItem);
    }


}
