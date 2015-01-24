package algorithms;

import java.util.HashMap;
import java.util.Map;

import patterns.Alphabet;
import patterns.Genome;
import patterns.Strand;
import algorithms.util.StrandOccurences;
import bases.Base;

/**
 * Classe representant l'algorithme Karp-Rabin
 */
public class KarpRabinAlgorithm extends Algorithm {

	// les valeurs des lettres de l'alphabet
	private Map<Character, Integer> lettersValues;
	// le resultat de la fonction de hachage hash() pour le brin recherche
	private int strandHash;

	/**
	 * construit l'algorithme Karp-Rabin
	 * 
	 * @param genome
	 *            le genome sur lequel s'effectuera la recherche
	 */
	public KarpRabinAlgorithm(Genome genome) {
		super(genome);
		final Alphabet alphabet = genome.getAlphabet();
		final Character[] alphabetLetters = alphabet.getLetters();
		lettersValues = new HashMap<Character, Integer>();
		for (int i = 0; i < alphabetLetters.length; i++) {
			lettersValues.put(alphabetLetters[i], i + 1);
		}
	}

	private int hash(Base[] bases) {
		final int alphabetLength = getGenome().getAlphabet().getSize();
		int currentCoeff = bases.length - 1;
		int finalHash = 0;
		for (final Base base : bases) {
			finalHash += lettersValues.get(base.getWording())
					* Math.pow(alphabetLength, currentCoeff);
			currentCoeff--;
		}
		return finalHash;
	}

	private void preTreat(Strand strand) {
		strandHash = hash(strand.getContent());
	}

	private boolean occurenceFound(Base[] basesToCompare, Base[] strandBases) {
		for (int i = 0; i < strandBases.length; i++) {
			if (!basesToCompare[i].equals(strandBases[i]))
				return false;
		}
		return true;
	}

	@Override
	public StrandOccurences findRepetitiveStrand(Strand strand) {
		preTreat(strand);
		final StrandOccurences strandOccurences = new StrandOccurences();
		final Base[] basesToCompare = new Base[strand.getSize()];
		for (int i = 0; i < getGenome().getSize() - strand.getSize() + 1; i++) {
			System.arraycopy(getGenome().getBases(), i, basesToCompare, 0,
					strand.getSize());
			final int basesHash = hash(basesToCompare);
			if ((basesHash == strandHash)
					&& (occurenceFound(basesToCompare, strand.getContent())))
				strandOccurences.addIndex(i);
		}
		return strandOccurences;
	}

	@Override
	public String toString() {
		return "Algorithme de Karp-Rabin";
	}

}
