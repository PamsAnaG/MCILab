/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.nlp;

import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTree;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.LangIdent;
import edu.upc.freeling.ListSentence;
import edu.upc.freeling.ListSentenceIterator;
import edu.upc.freeling.ListWord;
import edu.upc.freeling.ListWordIterator;
import edu.upc.freeling.Maco;
import edu.upc.freeling.MacoOptions;
import edu.upc.freeling.Nec;
import edu.upc.freeling.ParseTree;
import edu.upc.freeling.SWIGTYPE_p_splitter_status;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Sentence;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.Ukb;
import edu.upc.freeling.Util;
import edu.upc.freeling.Word;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pam
 */
public class NLProcessor {

    public void analizaTexto(String texto) {

        try {

            System.loadLibrary("freeling_javaAPI");

            String FLDIR = System.getenv("FREELINGDIR");
            if (FLDIR == null) {
                FLDIR = "/usr/local";
                System.err.println("FREELINGDIR environment variable not defined, trying " + FLDIR);
            }

            File f = new File(FLDIR + "/share/freeling");
            if (!f.exists()) {
                System.err.println("Folder " + FLDIR + "/share/freeling not found.");
                System.err.println("Please set FREELINGDIR environment variable to FreeLing installation directory");
                System.exit(1);
            }

            String DATA = FLDIR + "/share/freeling/";

            System.out.println("Directorio " + DATA);

            Util.initLocale("default");

            String LANG = "es";
            MacoOptions op = new MacoOptions(LANG);

            op.setDataFiles("",
                    DATA + "common/punct.dat",
                    DATA + LANG + "/dicc.src",
                    DATA + LANG + "/afixos.dat",
                    "",
                    DATA + LANG + "/locucions.dat",
                    DATA + LANG + "/np.dat",
                    DATA + LANG + "/quantities.dat",
                    DATA + LANG + "/probabilitats.dat");

            // Create analyzers.
            // language detector. Used just to show it. Results are printed
            // but ignored (after, it is assumed language is LANG)
            LangIdent lgid = new LangIdent(DATA + "/common/lang_ident/ident-few.dat");

            Tokenizer tk = new Tokenizer(DATA + LANG + "/tokenizer.dat");
            Splitter sp = new Splitter(DATA + LANG + "/splitter.dat");
            SWIGTYPE_p_splitter_status sid = sp.openSession();

            Maco mf = new Maco(op);
            mf.setActiveOptions(false, true, true, true, // select which among created 
                    true, true, false, true, // submodules are to be used. 
                    true, true, true, true);  // default: all created submodules 
            // are used

            HmmTagger tg = new HmmTagger(DATA + LANG + "/tagger.dat", true, 2);
            ChartParser parser = new ChartParser(
                    DATA + LANG + "/chunker/grammar-chunk.dat");
            DepTxala dep = new DepTxala(DATA + LANG + "/dep_txala/dependences.dat",
                    parser.getStartSymbol());
            Nec neclass = new Nec(DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");

            Senses sen = new Senses(DATA + LANG + "/senses.dat"); // sense dictionary
            Ukb dis = new Ukb(DATA + LANG + "/ukb.dat"); // sense disambiguator

            // Make sure the encoding matches your input text (utf-8, iso-8859-15, ...)            
            String line = texto;
            System.out.println("Cadena: " + line);

            // Identify language of the text.  
            // Note that this will identify the language, but will NOT adapt
            // the analyzers to the detected language.  All the processing 
            // in the loop below is done by modules for LANG (set to "es" at
            // the beggining of this class) created above.
            String lg = lgid.identifyLanguage(line);
            System.out.println("-------- LANG_IDENT results -----------");
            System.out.println("Language detected (from first line in text): " + lg);

            if (line != null) {
                System.out.println("*** Analizando " + line);
                // Extract the tokens from the line of text.
                ListWord l = tk.tokenize(line);

                // Split the tokens into distinct sentences.
                ListSentence ls = sp.split(l);
                System.out.println(ls.size());

                // Perform morphological analysis
                mf.analyze(ls);

                // Perform part-of-speech tagging.
                tg.analyze(ls);

                // Perform named entity (NE) classificiation.
                neclass.analyze(ls);

                sen.analyze(ls);
                dis.analyze(ls);
                printResults(ls, "tagged");

                // Chunk parser
                parser.analyze(ls);
                printResults(ls, "parsed");

                // Dependency parser
                dep.analyze(ls);
                printResults(ls, "dep");

            }

            sp.closeSession(sid);
        } catch (Exception excp) {
            excp.printStackTrace();

        }
    }

    public List<ListSentence> getTaggerResults(List<String> tweets) {

        List<ListSentence> resultados = new ArrayList();

        try {

            System.loadLibrary("freeling_javaAPI");

            String FLDIR = System.getenv("FREELINGDIR");
            if (FLDIR == null) {
                FLDIR = "/usr/local";
            }

            File f = new File(FLDIR + "/share/freeling");
            if (!f.exists()) {
                System.err.println("Folder " + FLDIR + "/share/freeling not found.");
                System.err.println("Please set FREELINGDIR environment variable to FreeLing installation directory");
                System.exit(1);
            }

            String DATA = FLDIR + "/share/freeling/";

            Util.initLocale("default");

            String LANG = "es";
            MacoOptions op = new MacoOptions(LANG);

            op.setDataFiles("", DATA + "common/punct.dat", DATA + LANG + "/dicc.src",
                    DATA + LANG + "/afixos.dat", "", DATA + LANG + "/locucions.dat",
                    DATA + LANG + "/np.dat", DATA + LANG + "/quantities.dat", DATA + LANG + "/probabilitats.dat");

            Tokenizer tk = new Tokenizer(DATA + LANG + "/tokenizer.dat");
            Splitter sp = new Splitter(DATA + LANG + "/splitter.dat");
            SWIGTYPE_p_splitter_status sid = sp.openSession();

            Maco mf = new Maco(op);
            mf.setActiveOptions(false, true, true, true, true, true, false, true, true, true, true, true);

            HmmTagger tg = new HmmTagger(DATA + LANG + "/tagger.dat", true, 2);

            Nec neclass = new Nec(DATA + LANG + "/nerc/nec/nec-ab-poor1.dat");

            Senses sen = new Senses(DATA + LANG + "/senses.dat");
            Ukb dis = new Ukb(DATA + LANG + "/ukb.dat");

            ListSentence ls = null;
            for (String tweet : tweets) {                
                ListWord l = tk.tokenize(tweet);

                ls = sp.split(l);

                mf.analyze(ls);

                tg.analyze(ls);

                neclass.analyze(ls);

                sen.analyze(ls);
                dis.analyze(ls);

                resultados.add(ls);
            }

            sp.closeSession(sid);
            return resultados;
        } catch (Exception excp) {
            excp.printStackTrace();

        }

        return null;

    }

    private static void printSenses(Word w) {
        String ss = w.getSensesString();

        // The senses for a FreeLing word are a list of
        // pair<string,double> (sense and page rank). From java, we
        // have to get them as a string with format
        // sense:rank/sense:rank/sense:rank
        // which will have to be splitted to obtain the info.
        //
        // Here, we just output it:
        System.out.print(" " + ss);
    }

    private static void printResults(ListSentence ls, String format) {

        if (format == "parsed") {
            System.out.println("-------- CHUNKER results -----------");

            ListSentenceIterator sIt = new ListSentenceIterator(ls);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();
                ParseTree tree = s.getParseTree();
                printParseTree(0, tree);
            }
        } else if (format == "dep") {
            System.out.println("-------- DEPENDENCY PARSER results -----------");

            ListSentenceIterator sIt = new ListSentenceIterator(ls);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();
                DepTree tree = s.getDepTree();
                printDepTree(0, tree);
            }
        } else {
            System.out.println("-------- TAGGER results -----------");

            // get the analyzed words out of ls.
            ListSentenceIterator sIt = new ListSentenceIterator(ls);
            while (sIt.hasNext()) {
                Sentence s = sIt.next();
                ListWordIterator wIt = new ListWordIterator(s);
                while (wIt.hasNext()) {
                    Word w = wIt.next();

                    System.out.print(
                            w.getForm() + " " + w.getLemma() + " " + w.getTag());
                    //printSenses(w);
                    System.out.println();
                }

                System.out.println();
            }
        }
    }

    private static void printParseTree(int depth, ParseTree tr) {
        Word w;
        long nch;

        // Indentation
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }

        nch = tr.numChildren();

        if (nch == 0) {
            // The node represents a leaf
            if (tr.begin().getInformation().isHead()) {
                System.out.print("+");
            }
            w = tr.begin().getInformation().getWord();
            System.out.print("(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
            //printSenses(w);
            System.out.println(")");
        } else {
            // The node is non-terminal
            if (tr.begin().getInformation().isHead()) {
                System.out.print("+");
            }

            System.out.println(tr.begin().getInformation().getLabel() + "_[");

            for (int i = 0; i < nch; i++) {
                ParseTree child = tr.nthChildRef(i);

                if (!child.empty()) {
                    printParseTree(depth + 1, child);
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }

            System.out.println("]");
        }
    }

    private static void printDepTree(int depth, DepTree tr) {
        DepTree child = null;
        DepTree fchild = null;
        long nch;
        int last, min;
        Boolean trob;

        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }

        System.out.print(
                tr.begin().getLink().getLabel() + "/"
                + tr.begin().getLabel() + "/");

        Word w = tr.begin().getWord();

        System.out.print(
                "(" + w.getForm() + " " + w.getLemma() + " " + w.getTag());
        //printSenses(w);
        System.out.print(")");

        nch = tr.numChildren();

        if (nch > 0) {
            System.out.println(" [");

            for (int i = 0; i < nch; i++) {
                child = tr.nthChildRef(i);

                if (child != null) {
                    if (!child.begin().isChunk()) {
                        printDepTree(depth + 1, child);
                    }
                } else {
                    System.err.println("ERROR: Unexpected NULL child.");
                }
            }

            // Print chunks (in order)
            last = 0;
            trob = true;

            // While an unprinted chunk is found, look for the one with lower
            // chunk_ord value.
            while (trob) {
                trob = false;
                min = 9999;

                for (int i = 0; i < nch; i++) {
                    child = tr.nthChildRef(i);

                    if (child.begin().isChunk()) {
                        if ((child.begin().getChunkOrd() > last)
                                && (child.begin().getChunkOrd() < min)) {
                            min = child.begin().getChunkOrd();
                            fchild = child;
                            trob = true;
                        }
                    }
                }
                if (trob && (child != null)) {
                    printDepTree(depth + 1, fchild);
                }

                last = min;
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }

            System.out.print("]");
        }

        System.out.println("");

    }

    public static void main(String argv[]) throws IOException {

        NLProcessor processor = new NLProcessor();
        //processor.analizaTexto("Todos nuestros trajes de baño te están esperando, ingresa ya mismo a nuestra web para que veas todo lo hermoso que tenemos para ti.");
        processor.analizaTexto("Buenos dias amigos de twitta Yo gibran soy un poco introvertido hasta cierto punto a veces timido Sin embargo en confianza y con buena vibra se activa mi mod extrovertido ¿sere el unico loco o hay algun esposo que pase por lo mismo Saludos y buen fin de semana");
    }

}
