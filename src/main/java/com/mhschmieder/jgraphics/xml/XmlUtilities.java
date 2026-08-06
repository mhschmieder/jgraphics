/*
 * MIT License
 *
 * Copyright (c) 2020, 2026 Mark Schmieder. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the jgraphics Library
 *
 * You should have received a copy of the MIT License along with the jgraphics
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/jgraphics
 */
package com.mhschmieder.jgraphics.xml;

import com.mhschmieder.jcommons.io.IoUtilities;
import com.mhschmieder.jcommons.io.ZipUtilities;
import com.mhschmieder.jcommons.lang.NumberUtilities;
import com.mhschmieder.jcommons.lang.StringUtilities;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * {@code XmlUtilities} is a static utilities class for common XML functionality
 * that at least wasn't part of Core Java at the time this library was written.
 * <p>
 * In particular, this class contains utilities related to grabbing XML content
 * from ZIP files, and converting XML to HTML (e.g. for displaying in WebKit).
 */
public class XmlUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities
     * class.
     */
    private XmlUtilities() {
    }

    /**
     * Given a string, replace all the instances of XML special characters with
     * their corresponding XML entities. This is necessary to allow arbitrary
     * strings to be encoded within XML.
     * <p>
     *
     * @param string The string to escape.
     * @return A new string with special characters replaced.
     * @see #unescapeForXML(String)
     */
    public static String escapeForXML( final String string ) {
        // This method gets called quite a bit when parsing large files, so
        // rather than calling substitute() many times, we combine all the loops
        // in one pass.

        // TODO: Find a way to get this into Javadocs without causing errors
        //
        // In this method, we make the following translations:
        //
        // &amp; becomes &amp;amp;
        // " becomes &amp;quot;
        // < becomes &amp;lt;
        // > becomes &amp;gt;
        // newline becomes &amp;#10;
        // carriage return becomes &amp;#13;

        // A different solution might be to scan the string for escaped XML
        // characters and if any are found, then create a StringBuilder and do
        // the conversion. Using a profiler would help here.
        if ( string == null ) {
            return null;
        }

        final StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0;
        int length = string.length();
        while ( i < length ) {
            switch ( stringBuilder.charAt( i ) ) {
                case '\n':
                    stringBuilder.deleteCharAt( i );
                    stringBuilder.insert( i, "&#10;" );
                    length += 4;
                    break;
                case '\r':
                    stringBuilder.deleteCharAt( i );
                    stringBuilder.insert( i, "&#13;" );
                    length += 4;
                    break;
                case '"':
                    stringBuilder.deleteCharAt( i );
                    stringBuilder.insert( i, "&quot;" );
                    length += 5;
                    break;
                case '&':
                    stringBuilder.deleteCharAt( i );
                    stringBuilder.insert( i, "&amp;" );
                    length += 4;
                    break;
                case '<':
                    stringBuilder.deleteCharAt( i );
                    stringBuilder.insert( i, "&lt;" );
                    length += 3;
                    break;
                case '>':
                    stringBuilder.deleteCharAt( i );
                    stringBuilder.insert( i, "&gt;" );
                    length += 3;
                    break;
                default:
                    break;
            }
            i++;
        }

        return stringBuilder.toString();
    }

    /**
     * Given a string, replace all the instances of XML entities with their
     * corresponding XML special characters. This is necessary to allow
     * arbitrary strings to be encoded within XML.
     *
     * @param string The string to escape.
     * @return A new string with special characters replaced.
     * @see #escapeForXML(String)
     */
    public static String unescapeForXML( final String string ) {
        // TODO: Find a way to get this into Javadocs without causing errors
        //
        // In this method, we make the following translations:
        //
        // &amp;amp; becomes &amp
        // &amp;quot; becomes "
        // &amp;lt; becomes <
        // &amp;gt; becomes >
        // &amp;#10; becomes newline
        // &amp;#13; becomes carriage return
        String unescapeString = string;
        if ( string.indexOf( '&' ) != -1 ) {
            unescapeString = StringUtilities.substitute( unescapeString,
                                                         "&amp;",
                                                         "&" );
            unescapeString = StringUtilities.substitute( unescapeString,
                                                         "&quot;",
                                                         "\"" );
            unescapeString = StringUtilities.substitute( unescapeString,
                                                         "&lt;",
                                                         "<" );
            unescapeString = StringUtilities.substitute( unescapeString,
                                                         "&gt;",
                                                         ">" );
            unescapeString = StringUtilities.substitute( unescapeString,
                                                         "&#10;",
                                                         "\n" );
            unescapeString = StringUtilities.substitute( unescapeString,
                                                         "&#13;",
                                                         "\r" );
        }
        return unescapeString;
    }

    public static void setTextValue( final Element rootElement,
                                     final String TagName,
                                     final String textValue ) {
        if ( ( textValue != null ) && ( !textValue.isEmpty() ) ) {
            rootElement.getElementsByTagName( TagName )
                       .item( 0 )
                       .getFirstChild()
                       .setNodeValue( textValue );
        }
    }

    public static double getElementDouble( final String tagName,
                                           final Document domDocument ) {
        final Element root = domDocument.getDocumentElement();
        final Element child = ( Element ) root.getElementsByTagName( tagName )
                                              .item( 0 );

        return NumberUtilities.parseDouble( child.getTextContent() );
    }

    public static int getElementInteger( final String tagName,
                                         final Document domDocument ) {
        final Element root = domDocument.getDocumentElement();
        final Element child = ( Element ) root.getElementsByTagName( tagName )
                                              .item( 0 );

        return NumberUtilities.parseInteger( child.getTextContent() );
    }

    public static String getElementString( final String tagName,
                                           final Document domDocument ) {
        final Element root = domDocument.getDocumentElement();
        final Element child = ( Element ) root.getElementsByTagName( tagName )
                                              .item( 0 );

        return child.getTextContent();
    }

    // NOTE: This method only works with JAR-resident XSLT files.
    // TODO: Rewrite this so that it works with XSLT's in any file location.
    public static boolean convertXmlToHtml( final File file,
                                            final StringBuilder htmlBuffer,
                                            final String jarRelativeXsltFilename ) {
        final StringBuilder fileContent = new StringBuilder( 1024 );

        final String fileName = file.getName();
        final String fileNameCaseInsensitive
                = fileName.toLowerCase( Locale.ENGLISH );
        if ( FilenameUtils.isExtension( fileNameCaseInsensitive, "xml" ) ) {
            // Load the XML file into an in-memory string builder.
            final boolean fileOpened = IoUtilities.loadIntoStringBuilder( file,
                                                                          fileContent );
            if ( !fileOpened ) {
                return false;
            }
        }
        else if ( FilenameUtils.isExtension( fileNameCaseInsensitive,
                                             "zip" ) ) {
            // Load the XML data from a ZIP file. Send the file vs. a
            // ZipInputStream, due to the need to cycle twice, and due to
            // problems with ZipInputStream.
            final boolean fileOpened
                    = ZipUtilities.loadZipToStringBuilder( file,
                                                           fileContent,
                                                           "xml" );
            if ( !fileOpened ) {
                return false;
            }
        }

        if ( fileContent.length() <= 0 ) {
            return false;
        }

        // Need to remove UTF-8 encoding as it will break the parser if it's not
        // actually UTF-8.
        // TODO: Review this decision, and all that follows.
        final String fileContentString = fileContent.toString()
                                                    .replaceAll( "(?i)encoding"
                                                                 + "=\"UTF-8\"",
                                                                 "encoding"
                                                                 + "=\"ISO"
                                                                 + "-8859-1"
                                                                 + "\"" );

        // Parse the file content string into an XML-DOM Document.
        try {
            final DocumentBuilderFactory docBuilderFactory
                    = DocumentBuilderFactory.newInstance();
            final DocumentBuilder docBuilder
                    = docBuilderFactory.newDocumentBuilder();
            final Document doc
                    = docBuilder.parse( new InputSource( new StringReader(
                    fileContentString ) ) );

            // Apply the XSLT to the XML to produce an HTML formatted
            // equivalent.
            final StringWriter html = new StringWriter( 1024 );
            try ( final InputStream xsltStream =
                          XmlUtilities.class.getResourceAsStream(
                    jarRelativeXsltFilename ) ) {
                final TransformerFactory xformerFactory
                        = TransformerFactory.newInstance();
                final Transformer xformer
                        = xformerFactory.newTransformer( new StreamSource(
                        xsltStream ) );
                xformer.transform( new DOMSource( doc ),
                                   new StreamResult( html ) );
                htmlBuffer.append( html.toString() );
            }
            catch ( final TransformerException te ) {
                // NOTE: We catch this exception separately because it needs
                //  special handling in order to dump useful information.
                System.err.println( XmlUtilities.getTransformerExceptionDetails(
                        te ) );
                return false;
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getTransformerExceptionDetails( final TransformerException te ) {
        final StringBuilder builder = new StringBuilder();
        builder.append( te );
        builder.append( " at " );
        builder.append( te.getLocationAsString() );
        final Throwable clause = te.getCause();
        if ( ( clause != null ) && !clause.equals( te ) ) {
            builder.append( '\n' );
            builder.append( clause );
        }
        return builder.toString();
    }
}
