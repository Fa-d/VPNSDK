package de.blinkt.util.io.pem;

public interface PemObjectGenerator {
    PemObject generate()
            throws PemGenerationException;
}
