package quaap.com.bookymcbookface.book;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tom on 9/16/17.
 */

public class TxtBook extends Book {
    List<String> l = new ArrayList<>();

    public TxtBook(Context context, File dataDir) {
        super(context, dataDir);
    }

    @Override
    protected void load() throws FileNotFoundException {
        if (!getFile().exists() || !getFile().canRead()) {
            throw new FileNotFoundException(getFile() + " doesn't exist or not readable");
        }
    }

    @Override
    public Map<String, String> getToc() {
        return null;
    }

    @Override
    protected BookMetadata getMetaData() throws IOException {
        BookMetadata metadata = new BookMetadata();
        metadata.setFilename(getFile().getPath());

        try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
            int c = 0;
            String line;
            Pattern titlerx = Pattern.compile("^\\s*(?i:title)[:= \\t]+(.+)");
            Pattern authorrx = Pattern.compile("^\\s*(?i:author|by)[:= \\t]+(.+)");
           // Pattern titleauthorrx = Pattern.compile("^(?xi: \\s* (.+),? \\s+ (?:translated\\s+)? by \\s+ (.+) )");

            boolean foundtitle = false;
            boolean foundauthor = false;
            while( (line=reader.readLine())!=null) {
//                Matcher tam = titleauthorrx.matcher(line);
//                if (tam.find()) {
//                    metadata.setTitle(tam.group(1));
//                    metadata.setAuthor(tam.group(2));
//                    foundtitle = true;
//                    foundauthor = true;
//                }

                Matcher tm = titlerx.matcher(line);
                if (!foundtitle && tm.find()) {
                    metadata.setTitle(tm.group(1));
                    foundtitle = true;
                }
                Matcher am = authorrx.matcher(line);
                if (!foundauthor && am.find()) {
                    metadata.setAuthor(am.group(1));
                    foundauthor = true;
                }
                if (c++>1000 || foundauthor && foundtitle) {
                    break;
                }

            }

            if (!foundtitle) {
                metadata.setTitle(getFile().getName());
            }
        }

        //metadata.setTitle(getFile().getName());
        return metadata;
    }

    @Override
    protected List<String> getSectionIds() {

        l.add("1");
        return l;
    }

    @Override
    protected File getFileForSectionID(String id) {
        return getFile();
    }

    @Override
    protected File getFileForSection(String section) {
        return getFile();
    }

    @Override
    protected String getSectionIDForSection(String section) {
        return "1";
    }



}