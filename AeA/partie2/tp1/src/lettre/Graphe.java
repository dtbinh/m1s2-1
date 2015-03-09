package lettre;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Classe représentant un graphe de mots
 * 
 * @author diagne
 */
public class Graphe {

	private MotGraphe[] mots;
	private int sup;
	private int dif;

	/**
	 * construit un graphe de mots
	 * 
	 * @param lesMots
	 *            le tableau de mots du graphe
	 * @param sup
	 *            le nombre de suppressions maximal pour que deux mots soient
	 *            voisins
	 * @param dif
	 *            le nombre de substitutions maximal pour que deux mots soient
	 *            voisins
	 */
	public Graphe(String[] lesMots, int sup, int dif) {
		final int nbMots = lesMots.length;
		mots = new MotGraphe[nbMots];
		for (int i = 0; i < nbMots; i++) {
			mots[i] = new MotGraphe(lesMots[i]);
		}
		this.sup = sup;
		this.dif = dif;
		initListeSuccesseursTousMots();
	}

	/**
	 * renvoie la liste des mots représentant le plus court chemin entre les
	 * mots d'indices <code>indiceFrom</code> et <code>indiceTo</code>
	 * 
	 * @param indiceFrom
	 *            l'indice de départ
	 * @param indiceTo
	 *            l'indice d'arrivée
	 * @return renvoie la liste des mots représentant le plus court chemin
	 */
	private Deque<String> getListeMotsFromTo(int indiceFrom, int indiceTo) {
		final Deque<String> listeMots = new ArrayDeque<String>();
		while (indiceTo != indiceFrom) {
			listeMots.addFirst(mots[indiceTo].getLibelle());
			indiceTo = mots[indiceTo].getPere();
		}
		listeMots.addFirst(mots[indiceFrom].getLibelle());
		return listeMots;
	}

	/**
	 * renvoie le mot à l'indice <code>i</code>
	 * 
	 * @param i
	 *            l'indice
	 * @return le mot à l'indice <code>i</code>
	 */
	public MotGraphe getMot(int i) {
		return mots[i];
	}

	/**
	 * initialise la liste des successeurs du mot à l'indice
	 * <code>indiceMot</code>
	 * 
	 * @param indiceMot
	 *            l'indice du mot
	 */
	public void initListeSuccesseursMot(int indiceMot) {
		for (int i = indiceMot; i < mots.length; i++) {
			final Entry<Integer, Integer> operations = Graphe
					.getNbreOperations(mots[i].getLibelle(),
							mots[indiceMot].getLibelle());
			if (operations.getKey() <= sup && operations.getValue() <= dif)
				ajouteArete(i, indiceMot);
		}
	}

	/**
	 * initialise la liste des successeurs des mots du graphe
	 */
	public void initListeSuccesseursTousMots() {
		for (int i = 0; i < mots.length; i++) {
			initListeSuccesseursMot(i);
		}
	}

	/**
	 * affiche le contenu d'une file de mots
	 * 
	 * @param listeMots
	 *            la file de mots
	 */
	public static void printListeMots(Deque<String> listeMots) {
		final Iterator<String> iterateurList = listeMots.iterator();
		while (iterateurList.hasNext()) {
			System.out.print(iterateurList.next() + " ");
		}
		System.out.println();
	}

	/**
	 * réinitialise les tableaux _dejaVu et _pere
	 */
	public void reset() {
		for (int i = 0; i < mots.length; i++) {
			final MotGraphe motI = mots[i];
			motI.setDejaVu(false);
			motI.setPere(-1);
		}
	}

	/**
	 * ajoute une arête entre deux sommets
	 * 
	 * @param s
	 *            un sommet
	 * @param d
	 *            un sommet
	 */
	public void ajouteArete(int s, int d) {
		final MotGraphe motS = mots[s];
		final MotGraphe motD = mots[d];
		motS.addSuccesseur(d);
		motD.addSuccesseur(s);
	}

	/**
	 * teste si deux mots ne diffèrent que d'une lettre
	 * 
	 * @param mot1
	 *            un mot
	 * @param mot2
	 *            un mot
	 * @return renvoie <code>true</code> si <code>mot1</code> et
	 *         <code>mot2</code> ne diffèrent que d'une lettre,
	 *         <code>false</code> sinon
	 * @throws LongueursMotsDifferentesException
	 *             si les deux mots ne sont pas de la même longueur
	 */
	public static boolean diffUneLettre(String mot1, String mot2)
			throws LongueursMotsDifferentesException {
		if (mot1.length() != mot2.length()) {
			throw new LongueursMotsDifferentesException();
		}
		int nbDiff = 0;
		for (int i = 0; i < mot1.length(); i++) {
			if (mot1.charAt(i) != mot2.charAt(i))
				nbDiff++;
		}
		return (nbDiff == 1);
	}

	/**
	 * affiche tous les mots du graphe
	 */
	public void affiche() {
		System.out.println("Graphe.affiche()");
		for (int i = 0; i < mots.length; i++) {

			final MotGraphe motCourant = mots[i];
			System.out.printf("%2d", i);
			System.out.print(" : " + motCourant + " -> ");
			final List<Integer> listeSuccesseursMotCourant = motCourant
					.getListeSuccesseurs();
			final Iterator<Integer> iterateurListe = listeSuccesseursMotCourant
					.iterator();
			while (iterateurListe.hasNext()) {
				final int indiceSuccesseur = iterateurListe.next();
				System.out.print(mots[indiceSuccesseur] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * effectue un parcours en profondeur à partir d'un sommet
	 * 
	 * @param x
	 *            le sommet de départ
	 */
	public void dfs(int x) {
		mots[x].setDejaVu(true);
		System.out.print(mots[x] + " ");
		final List<Integer> listeSuccesseursMot = mots[x]
				.getListeSuccesseurs();
		final Iterator<Integer> iterateurListe = listeSuccesseursMot.iterator();
		while (iterateurListe.hasNext()) {
			final int y = iterateurListe.next();
			final MotGraphe motY = mots[y];
			if (!motY.dejaVu()) {
				motY.setPere(x);
				dfs(y);
			}
		}
	}

	/**
	 * affiche les composantes connexes du graphe
	 */
	public void visit() {
		System.out.println("Graphe.visit()");
		reset();
		int nbComposantes = 1;
		for (int i = 0; i < mots.length; i++) {
			if (!mots[i].dejaVu()) {
				System.out.printf("%2d: ", nbComposantes);
				dfs(i);
				System.out.println();
				nbComposantes++;
			}
		}
	}

	/**
	 * renvoie l'indice du mot dans le graphe
	 * 
	 * @param mot
	 *            le mot
	 * @return l'indice du mot dans le graphe
	 * @exception IllegalArgumentException
	 *                si le mot n'est pas dans le tableau
	 */
	public int getIndiceMot(String mot) throws IllegalArgumentException {
		for (int i = 0; i < mots.length; i++) {
			if (mot.equals(mots[i].getLibelle()))
				return i;
		}
		throw new IllegalArgumentException(mot + " n'est pas dans le tableau.");
	}

	/**
	 * recherche un plus court chemin entre deux mots
	 * 
	 * @param from
	 *            le mot de départ
	 * @param to
	 *            le mot d'arrivée
	 * @param verbose
	 *            <code>true</code> permet d'afficher le chemin s'il en existe,
	 *            !false pour pas d'affichage
	 * @return la file de mots représentant le sommet trouvé (<code>null</code>
	 *         si pas de chemin)
	 */
	public Deque<String> chemin(String from, String to, boolean verbose) {
		if (verbose) {
			System.out.println("Graphe.chemin()");
		}
		reset();
		int indiceFrom = getIndiceMot(from);
		int indiceTo = getIndiceMot(to);
		bfsIteratif(indiceFrom);
		if (mots[indiceTo].getPere() == -1) {
			if (verbose) {
				System.out.println("Il n'y a pas de chemin entre "
						+ mots[indiceFrom] + " et " + mots[indiceTo]);
			}
			return null;
		} else {
			final Deque<String> listeMots = getListeMotsFromTo(indiceFrom,
					indiceTo);
			if (verbose) {
				System.out.println("Plus court chemin (" + from + " - " + to
						+ ")");
				printListeMots(listeMots);
			}
			return listeMots;
		}
	}

	/**
	 * effectue un parcours en largeur du graphe à partir du sommet s
	 * 
	 * @param s
	 *            le sommet de départ
	 */
	public void bfsIteratif(int s) {
		final Deque<Integer> pileSommets = new ArrayDeque<Integer>();
		final int[] d = new int[mots.length];
		d[s] = 0;
		mots[s].setDejaVu(true);
		pileSommets.addLast(s);
		while (!pileSommets.isEmpty()) {
			final int x = pileSommets.removeFirst();
			final List<Integer> listeSuccesseursY = mots[x]
					.getListeSuccesseurs();
			final Iterator<Integer> iterateurListe = listeSuccesseursY
					.iterator();
			while (iterateurListe.hasNext()) {
				final int y = iterateurListe.next();
				final MotGraphe motY = mots[y];
				if (!motY.dejaVu()) {
					motY.setDejaVu(true);
					motY.setPere(x);
					d[y] = d[x] + 1;
					pileSommets.addLast(y);
				}
			}
		}
	}

	/**
	 * calcule la distance d'édition entre deux mots et renvoie la table de
	 * programmation dynamique
	 * 
	 * @param u
	 *            le mot de départ
	 * @param v
	 *            le mot d'arrivée
	 * @return la table de programmation dynamique
	 */
	public static int[][] construireTableLevenshtein(String u, String v) {
		final int n = u.length();
		final int m = v.length();
		final int[][] table = new int[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				if (i == 0) {
					table[i][j] = j;
				} else {
					if (j == 0) {
						table[i][j] = i;
					} else {
						if (u.charAt(i - 1) == v.charAt(j - 1)) {
							table[i][j] = table[i - 1][j - 1];
						} else {
							table[i][j] = 1 + Math.min(Math.min(
									table[i - 1][j - 1], table[i - 1][j]),
									table[i][j - 1]);
						}
					}
				}
			}
		}
		return table;
	}

	/**
	 * calcule le chemin d'excentricité maximale du graphe
	 * 
	 * @return la file de mots représentant le chemin d'excentricité maximale du
	 *         graphe
	 */
	public Deque<String> getCheminMaxExcentricite() {
		Deque<String> max = new ArrayDeque<String>();
		final boolean[] dejaTraite = new boolean[mots.length];
		for (int i = 0; i < mots.length; i++) {
			final Deque<String> cheminExcentriciteCourant = getExcentriciteMot(
					i, dejaTraite);
			if (cheminExcentriciteCourant != null
					&& cheminExcentriciteCourant.size() > max.size()) {
				max = cheminExcentriciteCourant;
			}
			dejaTraite[i] = true;
		}
		return max;
	}

	/**
	 * renvoie le nombre de suppressions et de substitutions faites pour aller
	 * d'un mot à l'autre
	 * 
	 * @param u
	 *            le mot de départ
	 * @param v
	 *            le mot d'arrivée
	 * @return une association clé-valeur représentant le nombre de suppressions
	 *         et de substitutions
	 */
	public static Entry<Integer, Integer> getNbreOperations(String u, String v) {
		int nbSup = 0;
		int nbDif = 0;
		final int n = u.length();
		final int m = v.length();
		final int[][] table = Graphe.construireTableLevenshtein(u, v);
		int i = n;
		int j = m;
		if (u.equals(v)) {
			return new AbstractMap.SimpleImmutableEntry<Integer, Integer>(0,
					1);
		}
		while (table[i][j] != 0 || i != 0 || j != 0) {
			if (i == 0) {
				j--;
			} else {
				if (j == 0) {
					nbSup++;
					i--;
				} else {
					if (u.charAt(i - 1) == v.charAt(j - 1)) {
						i--;
						j--;
					} else {
						if (table[i][j] == 1 + table[i][j - 1]) {
							j--;
						} else {
							if (table[i][j] == 1 + table[i - 1][j]) {
								nbSup++;
								i--;
							} else {
								nbDif++;
								i--;
								j--;
							}
						}
					}
				}
			}
		}
		return new AbstractMap.SimpleImmutableEntry<Integer, Integer>(nbSup,
				nbDif);
	}

	/**
	 * renvoie l'excentricité maximale d'un mot
	 * 
	 * @param i
	 *            l'indice du mot dans le graphe
	 * @param dejaTraite
	 *            un tableau de booléens permettant de savoir quel mot a déjà
	 *            été traité dans le graphe
	 * @return la file de mots représentant le chemin d'excentricité maximale
	 *         pour aller du mot d'indice <code>i</code> à un autre mot
	 */
	public Deque<String> getExcentriciteMot(int i, boolean[] dejaTraite) {
		final MotGraphe mot = mots[i];
		Deque<String> cheminExcentriciteMax = new ArrayDeque<String>();
		for (int j = 0; j < mots.length; j++) {
			if (j != i && !dejaTraite[j]) {
				final Deque<String> cheminIJ = chemin(mot.getLibelle(),
						mots[j].getLibelle(), false);
				if (cheminIJ != null
						&& cheminIJ.size() > cheminExcentriciteMax.size()) {
					cheminExcentriciteMax = cheminIJ;
				}
			}
		}
		return cheminExcentriciteMax;
	}

	/**
	 * affiche sur la sortie d'erreur comment utiliser le programme
	 */
	private static void usage() {
		System.err.println("java -jar lqs.jar dico mot1 mot2 sup dif");
		System.err.println("\tCe programme affiche le chemin le plus court entre mot1 et mot2");
		System.err.println("\tdans le graphe du fichier dico avec au plus sup suppressions");
	    System.err.println("\tet dif substitutions et le chemin d'excentricité maximale du graphe");
	}

	/**
	 * méthode principale
	 * 
	 * @param args
	 *            le tableau d'arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try {
			if (args.length < 5) {
				usage();
				return;
			}
			final String filename = args[0];
			final String mot1 = args[1];
			final String mot2 = args[2];
			final Integer sup = Integer.parseInt(args[3]);
			final Integer dif = Integer.parseInt(args[4]);
			final DicoReader dicoReader = new DicoReader();
			final List<String> motsGraphe = dicoReader.read(filename);
			final String[] motsGrapheArray = motsGraphe
					.toArray(new String[motsGraphe.size()]);
			final Graphe graphe = new Graphe(motsGrapheArray, sup, dif);
			System.out.println("***************************************");
			graphe.visit();
			System.out.println("***************************************");
			graphe.chemin(mot1, mot2, true);
			System.out.println("***************************************");
			System.out.println("Chemin d'excentricité maximale : ");
			Graphe.printListeMots(graphe.getCheminMaxExcentricite());
			System.out.println("***************************************");
		} catch (NumberFormatException e) {
			usage();
			return;
		}
	}

}
