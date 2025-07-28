# Shamir's Secret Sharing Implementation

A Java implementation of Shamir's Secret Sharing Scheme using Lagrange interpolation for threshold cryptography.

## Overview

This project implements a (k,n) threshold secret sharing system where:
- A secret is divided into `n` shares
- Any `k` shares can reconstruct the original secret
- Fewer than `k` shares reveal no information about the secret

## Features

- **Multi-base Encoding**: Supports shares encoded in different number bases (binary, octal, decimal, hexadecimal, etc.)
- **Arbitrary Precision**: Uses `BigInteger` for handling large cryptographic values
- **JSON Input**: Reads share data from structured JSON files
- **Lagrange Interpolation**: Mathematical reconstruction of the secret polynomial
- **Threshold Security**: Cryptographically secure k-out-of-n sharing

## Files

- `SecretFinder.java` - Main implementation with JSON parsing and secret reconstruction
- `ExplanationDemo.java` - Step-by-step demonstration of the Lagrange interpolation process
- `testcase1.json` - Sample input with 4 shares, threshold k=3
- `testcase2.json` - Sample input with 10 shares, threshold k=7

## Dependencies

- **json-simple**: JSON parsing library
  - Download: [json-simple-1.1.1.jar](https://repo1.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar)

## Compilation and Execution

### Windows (PowerShell)
```powershell
# Compile
javac -cp ".;json-simple-1.1.1.jar" SecretFinder.java

# Run
java -cp ".;json-simple-1.1.1.jar" SecretFinder
```

### Linux/macOS
```bash
# Compile
javac -cp ".:json-simple-1.1.1.jar" SecretFinder.java

# Run
java -cp ".:json-simple-1.1.1.jar" SecretFinder
```

## Input Format

JSON files should contain:
```json
{
  "keys": {
    "n": 4,    // Total number of shares
    "k": 3     // Minimum shares needed
  },
  "1": {
    "base": "10",    // Number base for this share
    "value": "4"     // Share value in specified base
  },
  "2": {
    "base": "2",
    "value": "111"   // Binary: 111₂ = 7₁₀
  }
  // ... more shares
}
```

## Mathematical Background

The implementation is based on:
1. **Polynomial Secret Sharing**: Secret is the constant term of a polynomial
2. **Lagrange Interpolation**: Reconstructs polynomial from k points
3. **Finite Field Arithmetic**: Ensures perfect secrecy properties

### Algorithm
Given k points (x₁,y₁), (x₂,y₂), ..., (xₖ,yₖ), the secret S is computed as:

```
S = Σ(i=1 to k) yᵢ × Π(j=1 to k, j≠i) (0-xⱼ)/(xᵢ-xⱼ)
```

## Example Output
```
Secret 1: 3
Secret 2: 79836264049851
```

## Security Properties

- **Perfect Secrecy**: (k-1) shares provide zero information about the secret
- **Threshold Access**: Any k shares can reconstruct the secret
- **Information Theoretic Security**: Security is mathematical, not computational

## Use Cases

- Cryptocurrency wallet backup
- Multi-party key management
- Secure distributed storage
- Corporate secret management
- Disaster recovery systems

## License

This project is for educational and research purposes.
