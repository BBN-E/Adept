package adept.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adept.common.Document;
import adept.common.Token;
import adept.common.TokenStream;
import adept.common.TokenOffset;
import adept.common.Pair;
import adept.common.Message;
import adept.common.HltContentContainer;

import adept.utilities.DocumentMaker;

// TODO: Auto-generated Javadoc
/**
 * The Class EmailReader.
 */
public class EmailReader {
    
    	/** The instance. */
	private static EmailReader instance;
	
	/**
	 * Gets the single instance of EmailReader.
	 * 
	 * @return single instance of EmailReader
	 */
	public static EmailReader getInstance() {
		if (instance == null)
			instance = new EmailReader();
		return instance;
	}

    /**
     * Map char to token offset.
     *
     * @param tokenStream the token stream
     * @return the hash map
     */
    private HashMap<Integer,Integer> mapCharToTokenOffset(TokenStream tokenStream)
    {
        HashMap<Integer,Integer> charTokenMap = new HashMap<Integer,Integer>();
        for(int x = 0; x < tokenStream.size(); x++)
        {
            Token t = tokenStream.get(x);
            for(int offset = t.getCharOffset().getBegin(); offset < t.getCharOffset().getEnd(); offset++)
            {
                charTokenMap.put(offset,x);
            }
        }
        return charTokenMap;
    }

    /**
     * Gets the token offset.
     *
     * @param charTokenMap the char token map
     * @param charStart the char start
     * @param charEnd the char end
     * @return the token offset
     */
    private TokenOffset getTokenOffset(HashMap<Integer,Integer> charTokenMap, int charStart, int charEnd)
    {
        Integer tokenStart;
        Integer tokenEnd;
        while((tokenStart = charTokenMap.get(charStart)) == null && charStart < charEnd)
            charStart++;
        while((tokenEnd = charTokenMap.get(charEnd)) == null && charEnd > charStart)
            charEnd--;
        if(tokenStart != null && tokenEnd != null)
        {
            return new TokenOffset(tokenStart,tokenEnd);
        }
        return null;
    }

    /**
     * Read message id.
     *
     * @param headerText the header text
     * @return the string
     */
    private String readMessageId(String headerText)
    {
        String idMatch = "Message-Id:[^@]*";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        if(idMatcher.find())
        {
            return headerText.substring(idMatcher.start(),idMatcher.end()).replace("\\D","");
        }
        else
            return "NO MESSAGE-ID";
    }


    /**
     * Read message sender.
     *
     * @param headerText the header text
     * @return the string
     */
    private String readMessageSender(String headerText)
    {
        String idMatch = "(FROM|from|From):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        if(idMatcher.find())
        {
            return headerText.substring(idMatcher.start(),idMatcher.end()).replace("(FROM|from|From):","").trim();
        }
        else
            return "NO SENDER NAME";
    }

    /**
     * Read message subject.
     *
     * @param headerText the header text
     * @return the string
     */
    private String readMessageSubject(String headerText)
    {
        String idMatch = "(SUBJECT|subject|Subject):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        if(idMatcher.find())
        {
            return headerText.substring(idMatcher.start(),idMatcher.end()).replace("(SUBJECT|subject|Subject):","").trim();
        }
        else
            return "NO SUBJECT";
    }

    /**
     * Read message date.
     *
     * @param headerText the header text
     * @return the string
     */
    private String readMessageDate(String headerText)
    {
        String idMatch = "(SENT|sent|Sent|DATE|date|Date):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        if(idMatcher.find())
        {
            return headerText.substring(idMatcher.start(),idMatcher.end()).replace("(SENT|sent|Sent|DATE|date|Date):","").trim();
        }
        else
            return "NO DATE";
    }

    /**
     * Read message priority.
     *
     * @param headerText the header text
     * @return the string
     */
    private String readMessagePriority(String headerText)
    {
        String idMatch = "(IMPORTANCE|importance|Importance|PRIORITY|priority|Priority):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        if(idMatcher.find())
        {
            return headerText.substring(idMatcher.start(),idMatcher.end()).replace("(IMPORTANCE|importance|Importance|PRIORITY|priority|Priority):","").trim();
        }
        else
            return "NO PRIORITY";
    }

    /**
     * Read message to.
     *
     * @param headerText the header text
     * @return the list
     */
    private List<String> readMessageTo(String headerText)
    {
        String idMatch = "(To|TO|to):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        ArrayList<String> elemList = new ArrayList<String>();
        if(idMatcher.find())
        {
            String members = headerText.substring(idMatcher.start(),idMatcher.end()).replace("(TO|to|To):","").trim().replaceAll(",",";");
            elemList = new ArrayList<String>(Arrays.asList(members.split(";")));
        }
        return elemList;
    }

    /**
     * Read message cc.
     *
     * @param headerText the header text
     * @return the list
     */
    private List<String> readMessageCc(String headerText)
    {
        String idMatch = "(Cc|CC|cc):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        ArrayList<String> elemList = new ArrayList<String>();
        if(idMatcher.find())
        {
            String members = headerText.substring(idMatcher.start(),idMatcher.end()).replace("(CC|cc|Cc):","").trim().replaceAll(",",";");
            elemList = new ArrayList<String>(Arrays.asList(members.split(";")));
        }
        return elemList;
    }

    /**
     * Read message bcc.
     *
     * @param headerText the header text
     * @return the list
     */
    private List<String> readMessageBcc(String headerText)
    {
        String idMatch = "(Bcc|BCC|bcc):[^<\\r\\n]+[<\\r\\n]";
        Matcher idMatcher = Pattern.compile(idMatch).matcher(headerText);
        ArrayList<String> elemList = new ArrayList<String>();
        if(idMatcher.find())
        {
            String members = headerText.substring(idMatcher.start(),idMatcher.end()).replace("(BCC|bcc|Bcc):","").trim().replaceAll(",",";");
            elemList = new ArrayList<String>(Arrays.asList(members.split(";")));
        }
        return elemList;
    }


    /**
     * Prune message.
     *
     * @param messageData the message data
     * @param fullText the full text
     * @param charTokenMap the char token map
     * @param tokenStream the token stream
     * @return the pair
     */
    private Pair<TokenOffset,String> pruneMessage(Pair<TokenOffset,String> messageData, String fullText, HashMap<Integer,Integer> charTokenMap, TokenStream tokenStream)
    {
        //try to find signature or other irrelevant text at end of message
        String sender = messageData.getR();
        int sectionStart = tokenStream.get(messageData.getL().getBegin()).getCharOffset().getBegin();
        int sectionEnd = tokenStream.get(messageData.getL().getEnd()).getCharOffset().getEnd();
        String message = fullText.substring(sectionStart,sectionEnd);

        String lineType = "(e-mail|E-mail|E-MAIL|Email|EMAIL|email|Tel|TEL|tel|Phone|phone|PHONE|Voice|voice|VOICE|home|Home|HOME|FAX|Fax|fax|f|F|o|O)";
        String indicativeString = "(" + lineType + "\\s?[:\\)][^\\r\\n]*[\\r\\n]{1,4})";
        String nonIndicativeString = "(\\w+[^\\r\\n]*[\\r\\n]{1,4})";
        String horizontalString = "((-{2,}|_{2,})[^\\r\\n]*[\\r\\n]{1,4})";
        String regexString = horizontalString + "?(" + nonIndicativeString + "{0,4}" + indicativeString + "+" + nonIndicativeString + "{0,4}){2,}";

        Matcher footerMatcher = Pattern.compile(regexString).matcher(message);

        if(footerMatcher.find())
        {
            TokenOffset newTO = getTokenOffset(charTokenMap, sectionStart,  footerMatcher.start()+sectionStart-1);
            //            System.out.println("FOOTER:\n"+message.substring(footerMatcher.start(),footerMatcher.end()));
            return new Pair<TokenOffset,String>(newTO,sender);
        }

        String phoneType = "(iPhone|Blackberry|BlackBerry|Android|Mobile|Cellular|Verizon|Sprint|AT&T|Virgin)";
        String phoneRegexString = "Sent from my[^" + phoneType + "]*" + phoneType + "[^\\r\\n]*[\\s-_]*";
        Matcher phoneMatcher = Pattern.compile(phoneRegexString).matcher(message);

        if(phoneMatcher.find())
        {
            if(phoneMatcher.end() >= message.length())
            {
                TokenOffset newTO = getTokenOffset(charTokenMap, sectionStart,  phoneMatcher.start()+sectionStart-1);
                //                System.out.println("FOOTER:\n"+message.substring(phoneMatcher.start(),phoneMatcher.end()));
                return new Pair<TokenOffset,String>(newTO,sender);
            }
        }
        return messageData;
    }


    /**
     * Read email text.
     *
     * @param fullText the full text
     * @param headerText the header text
     * @param charTokenOffset the char token offset
     * @return the array list
     */
    private ArrayList<Pair<TokenOffset,String>> readEmailText(String fullText, String headerText, HashMap<Integer,Integer> charTokenOffset)
    {
        ArrayList<Pair<TokenOffset,String>> messageTexts = new ArrayList<Pair<TokenOffset,String>>();

        String lineType = "(From|FROM|from|Sent|SENT|sent|To|TO|to|SUBJECT|subject|Subject|Importance|IMPORTANCE|importance|CC|Cc|cc|BCC|Bcc|BCc|bcc|DATE|date|Date|PRIORITY|priority|Priority)";
        String regexString = "((" + lineType + ":[^\\r\\n]*[\\r\\n]{1,2})+(\\w+:[^\\r\\n]*[\\r\\n]{1,2})*){3,}";

        Matcher headingMatcher = Pattern.compile(regexString).matcher(fullText);
        int lastEnd = 0;
        String sender = "";
        String date = "";
        String priority = "";
        String subject = "";
        List<String> recipients = new ArrayList<String>();
        List<String> ccRecipients = new ArrayList<String>();
        List<String> bccRecipients = new ArrayList<String>();
        while(headingMatcher.find())
        {
            sender = "NO SENDER";
            //find message author's name
            if(lastEnd == 0)
            {
                sender = readMessageSender(headerText);
                date = readMessageDate(headerText);
                priority = readMessagePriority(headerText);
                subject = readMessageSubject(headerText);
                recipients = readMessageTo(headerText);
                ccRecipients = readMessageCc(headerText);
                bccRecipients = readMessageBcc(headerText);
            }
            else
            {
                String thisHeaderText = fullText.substring(headingMatcher.start(),headingMatcher.end());
                sender = readMessageSender(thisHeaderText);
                date = readMessageDate(thisHeaderText);
                priority = readMessagePriority(thisHeaderText);
                subject = readMessageSubject(thisHeaderText);
                recipients = readMessageTo(thisHeaderText);
                ccRecipients = readMessageCc(thisHeaderText);
                bccRecipients = readMessageBcc(thisHeaderText);
            }
            //            System.out.println("HEADER STARTING AFTER: " + lastEnd);
            //            System.out.println(fullText.substring(headingMatcher.start(),headingMatcher.end()));

            TokenOffset messageOffset = getTokenOffset(charTokenOffset,  lastEnd, headingMatcher.start()-1);
            if(messageOffset != null)
                messageTexts.add(new Pair<TokenOffset,String>(messageOffset,sender));
            lastEnd = headingMatcher.end();
        }

        TokenOffset messageOffset = getTokenOffset(charTokenOffset,  lastEnd, fullText.length());
        if(messageOffset != null)
            messageTexts.add(new Pair<TokenOffset,String>(messageOffset,sender));
        return messageTexts;
    }

    /**
     * Read an email.
     *
     * @param messagePath path to message text, a plaintext file,
     * @param headerPath path to header associated with message, a plaintext file.
     * @param pruneFooter whether or not to try to remove footer from message.
     * @return an HltContentContainer with a TokenStream per message in thread
     */
    public HltContentContainer readEmail(String messagePath, String headerPath, boolean pruneFooter)
    {
        Document d;
        HltContentContainer hltcc = new HltContentContainer();

        Reader r = Reader.getInstance();
        System.out.println("MESSAGE PATH: " + messagePath);
        String fullText = r.readFileIntoString(messagePath);
        String headerText = r.readFileIntoString(headerPath);

        String docId = "NO HEADER!";
        if(headerPath != null)
        {
            docId = readMessageId(headerText);
        }
        d = new Document(docId, null, "Email", null, "English");
        ArrayList<TokenStream> tsList = new ArrayList<TokenStream>();
        TokenStream tokenStream = DocumentMaker.getInstance().tokenize(fullText, d);
        tsList.add(tokenStream);
        d.setTokenStreamList(tsList);
        d.setValue(fullText);

        HashMap<Integer,Integer> charTokenOffset = mapCharToTokenOffset(tokenStream);
        ArrayList<Pair<TokenOffset,String>> messageTexts = readEmailText(fullText, headerText, charTokenOffset);
        ArrayList<Message> messages = new ArrayList<Message>();
        for(int x = 0; x < messageTexts.size(); x++)
        {
            Pair<TokenOffset,String> thisMessage = messageTexts.get(x);
            if(pruneFooter)
                thisMessage = pruneMessage(thisMessage,fullText,charTokenOffset,tokenStream);
            TokenOffset to = thisMessage.getL();
            Message p = new Message(x, to,tokenStream);
            p.setContentType(thisMessage.getR());
            messages.add(p);
            //            System.out.println("MESSAGE: " + x);
            System.out.println(fullText.substring(tokenStream.get(to.getBegin()).getCharOffset().getBegin(),tokenStream.get(to.getEnd()).getCharOffset().getEnd()));
        }
        hltcc.setMessages(messages);

        return hltcc;
    }
}