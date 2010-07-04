package org.atlas.model.adapter.xml.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.digester.Digester;
import org.atlas.mda.adapter.Adapter;
import org.atlas.mda.adapter.AdapterException;
import org.atlas.metamodel.Association;
import org.atlas.metamodel.Boundary;
import org.atlas.metamodel.Entity;
import org.atlas.metamodel.Enumeration;
import org.atlas.metamodel.Literal;
import org.atlas.metamodel.Model;
import org.atlas.metamodel.Property;
import org.atlas.metamodel.Tag;
import org.xml.sax.SAXException;

/**
 *
 *
 */
public class TagAdapter implements Adapter {

    private List<Entity> entityList = new ArrayList<Entity>();
    private List<Boundary> boundaryList = new ArrayList<Boundary>();
    private List<Enumeration> enumList = new ArrayList<Enumeration>();
    private Model model;

    public Model adapt(File file, Model model) throws AdapterException {
        try {
            this.model = model;

            FileInputStream s = new FileInputStream(file);

            Digester d = new Digester();
            d.push(this);

            d.addObjectCreate("tags/entity", Entity.class);
            d.addSetProperties("tags/entity", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/entity", "addEntity");

            d.addObjectCreate("tags/entity/tag", Tag.class);
            d.addSetProperties("tags/entity/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/entity/tag", "addTag");

            d.addObjectCreate("tags/entity/property", Property.class);
            d.addSetProperties("tags/entity/property", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/entity/property", "addProperty");

            d.addObjectCreate("tags/entity/property/tag", Tag.class);
            d.addSetProperties("tags/entity/property/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/entity/property/tag", "addTag");

            d.addObjectCreate("tags/entity/association", Association.class);
            d.addSetProperties("tags/entity/association", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/entity/association", "addAssociation");

            d.addObjectCreate("tags/entity/association/tag", Tag.class);
            d.addSetProperties("tags/entity/association/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/entity/association/tag", "addTag");

            d.addObjectCreate("tags/boundary", Boundary.class);
            d.addSetProperties("tags/boundary", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/boundary", "addBoundary");

            d.addObjectCreate("tags/boundary/tag", Tag.class);
            d.addSetProperties("tags/boundary/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/boundary/tag", "addTag");

            d.addObjectCreate("tags/boundary/property", Property.class);
            d.addSetProperties("tags/boundary/property", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/boundary/property", "addProperty");

            d.addObjectCreate("tags/boundary/property/tag", Tag.class);
            d.addSetProperties("tags/boundary/property/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/boundary/property/tag", "addTag");

            d.addObjectCreate("tags/enumeration", Enumeration.class);
            d.addSetProperties("tags/enumeration", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/enumeration", "addEnumeration");

            d.addObjectCreate("tags/enumeration/tag", Tag.class);
            d.addSetProperties("tags/enumeration/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/enumeration/tag", "addTag");

            d.addObjectCreate("tags/enumeration/literal", Literal.class);
            d.addSetProperties("tags/enumeration/literal", new String[]{"name"}, new String[]{"name"});
            d.addSetNext("tags/enumeration/literal", "addLiteral");

            d.addObjectCreate("tags/enumeration/literal/tag", Tag.class);
            d.addSetProperties("tags/enumeration/literal/tag", new String[]{"name", "value", "type"}, new String[]{"name", "value", "type"});
            d.addSetNext("tags/enumeration/literal/tag", "addTag");

            d.parse(s);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TagAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TagAdapter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(TagAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return adapt();
    }

    public void addEntity(Entity e) {
        entityList.add(e);
    }

    public void addBoundary(Boundary b) {
        boundaryList.add(b);
    }

    public void addEnumeration(Enumeration e) {
        enumList.add(e);
    }

    private Model adapt() throws AdapterException {
        adaptEntities();
        adaptBoundaries();
        adaptEnumerations();

        return model;
    }

    private void adaptEntities() throws AdapterException {

        for (Entity e : entityList) {
            Entity entity = model.getEntity(e.getName());
            if (entity == null) {
                throw new AdapterException("Entity [" + e.getName() + "] not found in model to be adapted.");
            }
            for (Tag t : e.getTags().values()) {
                entity.addTag(t);
            }

            for (Property p : e.getProperties()) {
                Property property = entity.getProperty(p.getName());
                if (property == null) {
                    throw new AdapterException("Property [" + p.getName() + "] not found in model to be adapted.");
                }
                for (Tag t : p.getTags().values()) {
                    property.addTag(t);
                }
            }

            for (Association a : e.getAssociationsByName().values()) {
                    Association association = entity.getAssociation(a.getName());
                    if (association == null) {
                        throw new AdapterException("Association [" + a.getName() + "] not found in model to be adapted.");
                    }
                    for (Tag t : a.getTags().values()) {
                        association.addTag(t);
                        Logger.getLogger(TagAdapter.class.getName()).log(Level.INFO, "Adding tag [" + t.getName() + ", " + t.getValue() + "] to association [" + a.getName() + "]");
                    }
            }
        }
    }

    private void adaptBoundaries() throws AdapterException {

        for (Boundary b : boundaryList) {
            Boundary boundary = model.getBoundary(b.getName());
            if (boundary == null) {
                throw new AdapterException("Boundary [" + b.getName() + "] not found in model to be adapted.");
            }
            for (Tag t : b.getTags().values()) {
                boundary.addTag(t);
            }

            for (Property p : b.getProperties()) {
                Property property = boundary.getProperty(p.getName());
                if (property == null) {
                    throw new AdapterException("Property [" + p.getName() + "] not found in model to be adapted.");
                }
                for (Tag t : p.getTags().values()) {
                    property.addTag(t);
                }
            }
        }
    }

    private void adaptEnumerations() throws AdapterException {

        for (Enumeration e : enumList) {
            Enumeration enumeration = model.getEnumeration(e.getName());
            if (enumeration == null) {
                throw new AdapterException("Enumeration [" + e.getName() + "] not found in model to be adapted.");
            }
            for (Tag t : e.getTags().values()) {
                enumeration.addTag(t);
            }

            for (Literal l : e.getLiterals()) {
                Literal literal = enumeration.getLiteral(l.getName());
                if (literal == null) {
                    throw new AdapterException("Literal [" + l.getName() + "] not found in model to be adapted.");
                }
                for (Tag t : l.getTags().values()) {
                    literal.addTag(t);
                }
            }
        }
    }
}
