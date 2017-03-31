/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.common.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;

/**
 *
 * @author Jakob
 */
public interface ITiledLoaderService {
    
    void load(ConcurrentHashMap<UUID, Entity> entities);
}
