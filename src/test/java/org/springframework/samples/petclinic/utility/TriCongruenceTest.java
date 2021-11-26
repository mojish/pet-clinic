package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
@ClauseDefinition(clause = 'a', def = "t1arr[0] == t2arr[0]")
@ClauseDefinition(clause = 'b', def = "t1arr[1] == t2arr[1]")
@ClauseDefinition(clause = 'c', def = "t1arr[2] == t2arr[2]")
@ClauseDefinition(clause = 'd', def = "t1arr[0] < 0")
@ClauseDefinition(clause = 'e', def = "t1arr[0] + t1arr[1] < t1arr[2]")
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	private void firstIfClause(double t10, double t11, double t12, double t20, double t21, double t22) {
		Triangle t1 = new Triangle(t10, t11, t12);
		Triangle t2 = new Triangle(t20, t21, t22);
		TriCongruence.areCongruent(t1, t2);
	}
	private void secondIfCause(double t10, double t11, double t12) {
		Triangle t1 = new Triangle(t10, t11, t12);
		Triangle t2 = new Triangle(t10, t11, t12);
		TriCongruence.areCongruent(t1, t2);
	}

	@ClauseCoverage(
		predicate = "d + e",
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		}
	)
	@Test
	public void CC_testcase1() {
		secondIfCause(-1, 4, 3);
	}
	@ClauseCoverage(
		predicate = "d + e",
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = true)
		}
	)
	@Test
	public void CC_testcase2() {
		secondIfCause(1, 2, 4);
	}

	// TF
	@CACC(
		predicate = "d + e",
		majorClause = 'd',
		valuations = {
			@Valuation(clause = 'd', valuation = true),
			@Valuation(clause = 'e', valuation = false)
		},
		predicateValue = true
	)
	@Test
	public void CACC_testcase1() {
		secondIfCause(-1, 4, 3);
	}

	// FF
	@CACC(
		predicate = "d + e",
		majorClause = 'd',
		valuations = {
			@Valuation(clause = 'd', valuation = false),
			@Valuation(clause = 'e', valuation = false)
		},
		predicateValue = false
	)
	@Test
	public void CACC_testcase2() {
		secondIfCause(1, 4, 3);
	}

	// FTT
	@UniqueTruePoint(
		predicate = "~a + ~b + ~c",
		dnf = "~a + ~b + ~c",
		implicant = "~a",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void testCUTPNFP1() {
		firstIfClause(1, 3, 7, 2, 3, 7);
	}

	// TFT
	@UniqueTruePoint(
		predicate = "~a + ~b + ~c",
		dnf = "~a + ~b + ~c",
		implicant = "~b",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void testCUTPNFP2() {
		firstIfClause(2, 4, 7, 2, 3, 7);
	}

	// TTF
	@UniqueTruePoint(
		predicate = "~a + ~b + ~c",
		dnf = "~a + ~b + ~c",
		implicant = "~c",
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = false)
		}
	)
	@Test
	public void testCUTPNFP3() {
		firstIfClause(2, 3, 6, 2, 3, 7);
	}

	// TTT
	@NearFalsePoint(
		predicate = "~a + ~b + ~c",
		dnf = "~a + ~b + ~c",
		implicant = "~a",
		clause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void testCUTPNFP4() {
		firstIfClause(1, 3, 7, 1, 3, 7);
	}

	// TTT
	@NearFalsePoint(
		predicate = "~a + ~b + ~c",
		dnf = "~a + ~b + ~c",
		implicant = "~a",
		clause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void testCUTPNFP5() {
		firstIfClause(1, 3, 7, 1, 3, 7);
	}

	// TTT
	@NearFalsePoint(
		predicate = "~a + ~b + ~c",
		dnf = "~a + ~b + ~c",
		implicant = "~a",
		clause = 'c',
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = true),
			@Valuation(clause = 'c', valuation = true)
		}
	)
	@Test
	public void testCUTPNFP6() {
		firstIfClause(1, 3, 7, 1, 3, 7);
	}


	/**
	 * f = ab + cd
	 * !f = !a!c + !a!d + !b!c + !b!d
	 *
	 * CUTPNFP test requirements: {TTFF, FFTT, TFFF, FTFF, FFTF, FFFT}
	 * UTPC test requirements:
	 * ab:{TTFF, TTFT, TTTF}
	 * cd:{FFTT, FTTT, TFTT}
	 * !a!c:{FTFT}
	 * !a!d:{FTTF}
	 * !b!c:{TFFT}
	 * !b!d:{TFTF}
	 * UTPC:{TTFF, TTFT, TTTF, FFTT, FTTT, TFTT, FTFT, FTTF, TFFT, TFTF}
	 *
	 *	CUTPNFP has UTPC test requirement for implicants in f only but UTPC requires UTPC test requirements for both f and !f
	 *  So CUTPNFP doesnt subsume UTPC. In the example above UTPC's requirements are not a subset of CUTPNFP's requirements.
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
		predicate = a&&b || c&&d;
		return predicate;
	}

}
