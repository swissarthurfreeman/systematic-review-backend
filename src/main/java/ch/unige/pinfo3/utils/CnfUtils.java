package ch.unige.pinfo3.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import io.quarkus.logging.Log;
import orbital.logic.imp.*;
import orbital.moon.logic.ClassicalLogic;
import orbital.logic.sign.ParseException;
import orbital.moon.logic.ClassicalLogic.Utilities;

public class CnfUtils {

    static final Logic logic = new ClassicalLogic();

    public static String createOrbitalFormula(String query) {
        query = query.replace(" AND ", " & ");
        query = query.replace(")AND ", ") & ");
        query = query.replace(" AND(", " & (");
        query = query.replace(" OR ", " | ");
        query = query.replace(")OR ", ") | ");
        query = query.replace(" OR(", " | (");
        if(query.startsWith("NOT"))
            query = query.replaceFirst("NOT", " ~ ");

        query = query.replace(" NOT ", " ~ ");
        query = query.replace(")NOT ", ") ~ ");
        query = query.replace(" NOT(", " ~ (");
        return query;
    }

    public static String toBackendString(String orbitalFormulaString) {
        orbitalFormulaString = orbitalFormulaString.replace("&", " AND ");
        orbitalFormulaString = orbitalFormulaString.replace("|", " OR ");
        orbitalFormulaString = orbitalFormulaString.replace("~", " NOT ");
        return orbitalFormulaString;
    }

    public static String sortCnf(String cnf) {
        cnf = cnf.replace(" ", "");
        var disjunctions = new ArrayList<>(Arrays.asList(cnf.split("&")));

        for (int i=0; i < disjunctions.size(); i++) {
            var atoms = new ArrayList<>(Arrays.asList(disjunctions.get(i).split("\\|")));
            
            // means it's not atomic
            if(atoms.size() > 1) { 
                atoms.set(0, atoms.get(0).replaceFirst("\\(", ""));
                atoms.set(atoms.size() - 1, atoms.get(atoms.size() - 1).replaceFirst("\\)", ""));

                Collections.sort(atoms);

                atoms.set(0, "(" + atoms.get(0)); 
                atoms.set(atoms.size() - 1,  atoms.get(atoms.size() - 1) + ")");
            }
            disjunctions.set(i, String.join("|", atoms));
        }
        Collections.sort(disjunctions);      // sorts by lexilographical order, thanks Comparable<String>
        return String.join("&", disjunctions);
    }

    public static String computeUcnf(String query) {
        try {
            Log.info("Computing ucnf of query = " + query);
            String formulaText = createOrbitalFormula(query);
            Formula formula = (Formula) logic.createExpression(formulaText);
            Formula cnf = Utilities.conjunctiveForm(formula, true);
            String ucnf = toBackendString(sortCnf(cnf.toString()));
            System.out.println("Computed ucnf = " + ucnf);
            return ucnf;    
        } catch(ParseException e) {
            Log.info("Error computing ucnf, returning query...");
            Log.error(e.getMessage());
            return query;
        }
    }
}
