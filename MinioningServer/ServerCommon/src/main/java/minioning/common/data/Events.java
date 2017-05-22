package minioning.common.data;

import java.io.Serializable;

/**
 *
 * @author Jakob and Mads
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
    ENEMYQ,
    CREATEMINION,
    MINIONQ,
    MINIONSWAP,
    FALSEEVENT
    
}
