package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import patterns.AlphabetTest;
import patterns.StrandTest;
import reader.FastaFileReaderTest;
import algorithms.AlgorithmsTest;

/**
 * Suite comportant l'ensemble des classes de tests
 */
@RunWith(Suite.class)
@SuiteClasses(value={
AlphabetTest.class,
StrandTest.class,
AlgorithmsTest.class,
FastaFileReaderTest.class
})
public class TestsSuite{
}
