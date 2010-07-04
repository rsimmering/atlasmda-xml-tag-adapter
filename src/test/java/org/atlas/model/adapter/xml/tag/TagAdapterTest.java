package org.atlas.model.adapter.xml.tag;

import org.atlas.model.adapter.xml.tag.TagAdapter;
import java.io.File;
import org.atlas.mda.Context;
import org.atlas.mda.ModelInput;
import org.atlas.mda.adapter.Adapter;
import org.atlas.mda.adapter.AdapterFactory;
import org.atlas.metamodel.Boundary;
import org.atlas.metamodel.Entity;
import org.atlas.metamodel.Enumeration;
import org.atlas.metamodel.Model;
import org.atlas.metamodel.Property;
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
        assertEquals(3, result.getEntity("Student").getTags().size());

        assertEquals("true", result.getBoundary("StudentView").getTagValue("dialog"));

        assertEquals("textArea", result.getBoundary("StudentView").getProperty("number").getTagValue("input"));

        assertEquals("Part Time", result.getEnumeration("StudentType").getLiteral("PartTime").getTagValue("label"));
    }
}
