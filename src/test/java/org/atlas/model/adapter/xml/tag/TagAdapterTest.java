package org.atlas.model.adapter.xml.tag;

import org.atlas.engine.Context;
import org.atlas.engine.ModelInput;
import org.atlas.model.adapter.Adapter;
import org.atlas.model.adapter.AdapterFactory;
import org.atlas.model.adapter.xml.tag.TagAdapter;
import org.atlas.model.metamodel.Boundary;
import org.atlas.model.metamodel.Entity;
import org.atlas.model.metamodel.Enumeration;
import org.atlas.model.metamodel.Model;
import org.atlas.model.metamodel.Property;

import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrews
 */
public class TagAdapterTest {

    public TagAdapterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of adapt method, of class TagAdapter.
     */
    @Test
    public void testAdapt() throws Exception {
        System.out.println("adapt");

        ModelInput mi = Context.getModelInputs().get(0);
        File f = new File(mi.getFile());
        Adapter a = AdapterFactory.getAdapter(mi.getAdapter());
        

        Model model = new Model();
        Entity e = new Entity();
        e.setName("Student");
        Property p = new Property();
        p.setName("number");
        e.addProperty(p);
        model.addEntity(e);

        Boundary b = new Boundary();
        b.setName("StudentView");
        p = new Property();
        p.setName("number");
        b.addProperty(p);
        model.addBoundary(b);

        Enumeration en = new Enumeration();
        en.setName("StudentType");
        en.addLiteral("PartTime");
        en.addLiteral("FullTime");
        model.addEnumeration(en);

        Model result = a.adapt(f, model);
        assertEquals(2, result.getEntity("Student").getTags().size());

        assertEquals("Part Time", result.getEnumeration("StudentType").getLiteral("PartTime").getTagValue("label"));
    }
}
