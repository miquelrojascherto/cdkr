package org.guha.rcdk.view;

import org.guha.rcdk.view.panels.MoleculeCell;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A one line summary.
 *
 * @author Rajarshi Guha
 */
public class MoleculeDisplay extends JPanel {
    int ncol = 2;
    int nrow = 2;
    int sep = 2;
    int width = 200;
    int height = 200;

    int nmol = 0;

    GridLayout layout;

    public MoleculeDisplay() {
        layout = new GridLayout(nrow, ncol, sep, sep);
        setLayout(layout);
    }

    public void setParams(String paramString) throws CDKException {
        parseParamString(paramString);
    }

    private void parseParamString(String paramString) throws CDKException {
        String[] lines = paramString.split("\n");
        for (String s : lines) {
            String[] toks = s.split("=");
            if (toks.length != 2) throw new CDKException("Invalid parameter string");
            String varName = toks[0].trim();
            String varValue = toks[1].trim();
            if (varName.equals("ncol")) ncol = Integer.parseInt(varValue);
            else if (varName.equals("nrow")) nrow = Integer.parseInt(varValue);
            else if (varName.equals("sep")) sep = Integer.parseInt(varValue);
            else if (varName.equals("width")) width = Integer.parseInt(varValue);
            else if (varName.equals("height")) height = Integer.parseInt(varValue);
        }
        layout = new GridLayout(nrow, ncol, sep, sep);
    }

    public void addMolecule(IAtomContainer molecule) {
        MoleculeCell cell = new MoleculeCell(molecule, width, height);
        add(cell);
    }

    public void addLabel(String label) {
        add(new JLabel(label));
    }

    public int getMoleculeCount() {
        return nmol;
    }

    public static void main(String[] args) throws CDKException {
        MoleculeDisplay md = new MoleculeDisplay();

        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        String[] smiles = {"CC(C)N(C(=O)CCl)c1ccccc1", "CC(C)N(C(=O)CCl)C1=CC=CC=C1", "CC(C)N(C(=O)CCl)C1CC=CCC1"};
        List<IAtomContainer> mols = new ArrayList<IAtomContainer>();

        IAtomContainer mol;
        for (String s : smiles) {
            mol = sp.parseSmiles(s);
            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
            sdg.setMolecule(mol);
            try {
                sdg.generateCoordinates();
            } catch (Exception e) {
            }
            mol = sdg.getMolecule();
            mols.add(mol);
        }


        //       md.setParams("ncol=2\nnrow=2");
        md.addMolecule(mols.get(0));
        md.addMolecule(mols.get(1));
        md.addMolecule(mols.get(2));
        md.addLabel("Foo");
        md.addLabel("Bar");
        md.addLabel("Baz");

        JFrame f = new JFrame("Molecule Display");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(md);
        f.pack();
        f.setVisible(true);
    }
}
