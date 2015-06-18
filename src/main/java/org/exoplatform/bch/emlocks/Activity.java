package org.exoplatform.bch.emlocks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by bdechateauvieux on 6/18/15.
 */
@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}
