package com.blogspot.carirunners.run.db;


import org.junit.After;
import org.junit.Before;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

abstract public class DbTest {
    protected AppDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase.class).build();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
