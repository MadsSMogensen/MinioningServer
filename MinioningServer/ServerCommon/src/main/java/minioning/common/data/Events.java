package minioning.common.data;

import java.io.Serializable;

/**
 *
 * @author Mogensen
 */
public enum Events implements Serializable{
    CREATEPLAYER,
    LOGIN,
    CREATEACCOUNT,
    LOGINSUCCESS,
    LOGINFAILED,
    MOVEMENT,
    PLAY,
    TESTEVENT,
    SKILLQ,
    CREATEMONSTER,
    HPCHANGE,
    FALSEEVENT
    
}
