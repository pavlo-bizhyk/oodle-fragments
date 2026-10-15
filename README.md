# Document Reassembly Service

A REST API built with Java 21 and Spring Boot 4 that reconstructs a document from overlapping text fragments.

The application uses a greedy algorithm to repeatedly merge fragments with the largest suffix-prefix overlap until a single string remains.

## Requirements

- Java 21 or later
- Maven (or the included Maven Wrapper)

## Running the Application

Clone the repository and navigate to the project directory.

Start the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

By default, the application runs at `http://localhost:8080`.

## API

### POST /reassemble

Reassembles text fragments into a single document.

**Request**

- Content-Type: `text/plain`
- Body: Semicolon-separated text fragments

Example:

```bash
curl -X POST http://localhost:8080/reassemble \
  -H "Content-Type: text/plain" \
  --data-raw "This ;s is an exam;xample problem.;blem."
```

**Response**

Status: `200 OK`

Content-Type: `text/plain`

```text
This is an example problem.
```

An empty HTTP request body returns `400 Bad Request`.

## Algorithm

The application uses a greedy approach:

1. Split the input into fragments using `;` as the separator.
2. Remove duplicate and blank fragments.
3. Remove fragments already contained within longer fragments.
4. Find the ordered pair of fragments with the largest suffix-prefix overlap.
5. Merge the pair, preserving the overlapping characters only once.
6. Remove newly redundant fragments.
7. Repeat until one fragment remains.

For example:

```text
Input:
abcXYZ;XYZdef;defghi

Step 1:
abcXYZ + XYZdef -> abcXYZdef

Step 2:
abcXYZdef + defghi -> abcXYZdefghi

Output:
abcXYZdefghi
```

If multiple pairs have the same overlap length, the algorithm selects the first pair encountered during iteration.

If no overlap exists, the fragments are concatenated.

### Complexity

For `n` fragments of maximum length `L`, the straightforward overlap calculation takes up to `O(L²)` time per pair.

A rough upper bound for the overall greedy algorithm is `O(n³ × L²)` time, excluding additional string-copying costs. This implementation prioritises simplicity and readability over performance on very large inputs.

## Edge Cases

The implementation handles:

- Duplicate fragments
- Fragments fully contained within other fragments
- Empty fragments between separators
- Whitespace at the beginning or end of nonblank fragments
- A single fragment
- No overlapping fragments
- Multiple successive merges

Whitespace-only fragments are discarded, while whitespace within nonblank fragments is preserved.

## Limitations

The greedy approach does not guarantee the shortest possible reconstruction. A locally optimal merge can result in a longer final string than another valid arrangement.

The original document may also be impossible to determine uniquely when fragments have insufficient or ambiguous overlaps.

Other limitations:

- Input order is not guaranteed when overlaps influence merge selection.
- Identical fragments are treated as duplicates, even if the original document contained repeated occurrences.
- Semicolons are reserved as fragment separators and cannot appear as literal fragment content.

## Testing

The project includes:

- **ReassemblyServiceTest:** Unit tests covering the merging algorithm and edge cases.
- **ReassemblyControllerTest:** MVC tests verifying the HTTP endpoint and empty-body handling.

Run all tests:

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## Logging

The application uses SLF4J with Spring Boot's default logging implementation.

- `INFO`: Incoming requests and completed responses.
- `DEBUG`: Fragment counts and merge operations.

Log levels can be configured in `src/main/resources/application.properties`:

```properties
logging.level.root=INFO
logging.level.org.example.reassembly=DEBUG
```

Request and response sizes are logged without including the document contents.