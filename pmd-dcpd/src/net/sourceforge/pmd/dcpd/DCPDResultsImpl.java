/*
 * User: tom
 * Date: Sep 11, 2002
 * Time: 11:35:19 AM
 */
package net.sourceforge.pmd.dcpd;

import net.sourceforge.pmd.cpd.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class DCPDResultsImpl extends TileOccurrences implements Results, Serializable {

    public void addTile(Tile tile, TokenEntry tok) {
        super.addTile(tile, tok);
        for (int i=orderedTiles.size()-1; i>=0; i--) {
            Tile candidate = (Tile)orderedTiles.get(i);
            removeDupesOf(candidate);
        }
    }

    public int getTileLineCount(Tile tile, TokenSets tokenSets) {
        TokenEntry firstToken = (TokenEntry)((List)tileToOccurrenceMap.get(tile)).get(0);
        TokenList tl = tokenSets.getTokenList(firstToken);
        TokenEntry lastToken = (TokenEntry)tl.get(firstToken.getIndex()-1 + tile.getTokenCount());
        return lastToken.getBeginLine() - firstToken.getBeginLine() - 1;
    }

    private void removeDupesOf(Tile largerTile) {
        for (int i=0; i<orderedTiles.size()-1; i++) {
            Tile smallerTile = (Tile)orderedTiles.get(i);

            outer:
            for (int j=0; j<smallerTile.getTokens().size(); j++) {
                TokenEntry smallTileToken = (TokenEntry)smallerTile.getTokens().get(j);

                for (int k=0; k<largerTile.getTokens().size(); k++) {
                    TokenEntry largeTileToken = (TokenEntry)largerTile.getTokens().get(k);
                    if (smallTileToken.getBeginLine() == largeTileToken.getBeginLine() &&
                        smallTileToken.getImage().equals(largeTileToken.getImage()) &&
                        smallTileToken.getTokenSrcID().equals(largeTileToken.getTokenSrcID())) {
                        super.orderedTiles.remove(smallerTile);
                        super.tileToOccurrenceMap.remove(smallerTile);
                        break outer;
                    }
                }
            }
        }
    }

/*
    private void removeDupesOf(Tile tile) {
        Set occs = new HashSet();
        occs.addAll((List)tileToOccurrenceMap.get(tile));
        for (Iterator i = tileToOccurrenceMap.keySet().iterator(); i.hasNext();) {
            Tile tile2 = (Tile)i.next();

            if (tile2.equals(tile)) {
                continue;
            }

            Set possibleDupe = new HashSet();
            possibleDupe.addAll((List)tileToOccurrenceMap.get(tile2));
            possibleDupe.removeAll(occs);
            if (possibleDupe.isEmpty()) {
                System.out.println("Removing dupe " + tile.getImage());
                tileToOccurrenceMap.remove(tile);
                orderedTiles.remove(tile);
                break;
            }
        }

    }
*/
}