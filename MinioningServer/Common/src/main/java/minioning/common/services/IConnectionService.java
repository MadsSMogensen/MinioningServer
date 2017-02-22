package minioning.common.services;

import minioning.common.data.EventBus;


/**
 *
 * @author Helle
 */
public interface IConnectionService {
    
    void process(EventBus eventBus);
}
