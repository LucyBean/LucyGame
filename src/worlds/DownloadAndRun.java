package worlds;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadAndRun {
	private static final String directoryName = "LucyGame Alpha Demo";

	public static void main(String[] args) {
		boolean dirExists;
		boolean success = true;

		try {
			dirExists = checkDir();
		} catch (IOException ioe) {
			System.err.println("Error: Unable to create directory for game.");
			return;
		}

		if (dirExists) {
			// Game was already downloaded
			Version vOnline = getOnlineVersion();
			Version vFile = getLocalVersion();

			System.out.println("Current version " + vFile);

			if (vOnline != null
					&& (vFile == null || vFile.compareTo(vOnline) < 0)) {
				// Online version is available and is newer than the local
				// version
				// Download latest game
				System.out.println("Downloading newest version " + vOnline);
				success = downloadGame();
			} else {
				System.out.println("Most recent version has been downloaded.");
			}
		} else {
			// Game has not been downloaded
			// Download all files
			success = downloadLibs();
			success = success && downloadGame();
		}

		if (success) {
			runGame();
		}

	}

	private static void runGame() {
		try {
			File directory = new File(directoryName);
			Runtime.getRuntime().exec(
					"cmd /c java -Djava.library.path=\"lib/slick-natives-windows\" -jar LucyGame.jar",
					null, directory.getAbsoluteFile());
			System.out.println("Running game...");
		} catch (IOException e) {
			System.err.println("Unable to run game " + e.getMessage());
		}
	}

	/**
	 * Checks if the game directory exists and creates it if not.
	 * 
	 * @param name
	 *            The directory to check.
	 * @return Whether or not the directory was created.
	 * @throws IOException
	 */
	private static boolean checkDir() throws IOException {
		String[] requiredDirs = new String[3];
		requiredDirs[0] = directoryName;
		requiredDirs[1] = directoryName + "/lib";
		requiredDirs[2] = directoryName + "/data";

		boolean allDirsExisted = true;

		for (String dir : requiredDirs) {
			File f = new File(dir);
			if (!f.exists()) {
				f.mkdir();
				System.out.println("Created new directory " + dir);
				allDirsExisted = false;
			}
		}
		return allDirsExisted;
	}

	private static boolean downloadLibs() {
		// Download the following file to the following folders:
		// lib/slick-native-windows.zip
		// runGame.bat
		boolean success = true;
		success = downloadFile("lib/slick-natives-windows.zip");
		success = success && unzipFile("lib/slick-natives-windows.zip");

		return success;
	}

	private static boolean downloadGame() {
		// Download the following files to the following folders:
		// LucyGame.jar
		// data/data-latest.zip
		boolean success = true;
		success = downloadFile("LucyGame.jar");
		success = success && downloadFile("data/data.zip");
		success = success && unzipFile("data/data.zip");
		success = success && downloadFile("build_info.properties");

		return success;
	}

	private static boolean downloadFile(String fileName) {
		try {
			System.out.print("Downloading " + fileName + "... ");
			URL url = new URL(
					"https://raw.githubusercontent.com/LucyBean/LucyGame/master/"
							+ fileName);
			BufferedInputStream in = new BufferedInputStream(url.openStream());
			FileOutputStream out = new FileOutputStream(
					directoryName + File.separator + fileName);

			byte[] data = new byte[1024];
			int count = 0;
			while ((count = in.read(data)) != -1) {
				out.write(data, 0, count);
			}

			in.close();
			out.close();

			System.out.println("Done.");
			return true;
		} catch (FileNotFoundException e) {
			System.out.println();
			System.err.println("Unable to find file " + e.getMessage());
		} catch (MalformedURLException e) {
			System.out.println();
			System.err.println("Malformed URL " + e.getMessage());
		} catch (IOException e) {
			System.out.println();
			System.err.println(
					"Error while downloading file " + fileName + ".");
			System.err.println("Please check your internet connection.");
		}
		return false;
	}

	private static boolean unzipFile(String zipName) {
		System.out.print("Unzipping " + zipName + "... ");
		File f = new File(directoryName + File.separator + zipName);
		String outputFolderName = f.getParent();
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(f));
			byte[] buffer = new byte[1024];

			// Get file
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				// Create the file
				String fileName = ze.getName();
				File newFile = new File(
						outputFolderName + File.separator + fileName);

				// Create directory
				if (ze.isDirectory()) {
					newFile.mkdir();
				}
				// Copy file
				else {
					newFile.getParentFile().mkdirs();

					FileOutputStream out = new FileOutputStream(newFile);

					int count = 0;
					while ((count = zis.read(buffer)) != -1) {
						out.write(buffer, 0, count);
					}
					out.close();
				}
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			System.out.println("Done.");
			return true;
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find file " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Unabled to unzip file" + e.getMessage());
		}

		return false;
	}

	/**
	 * Finds the version of the game on GitHub.
	 * 
	 * @return
	 */
	private static Version getOnlineVersion() {
		Version vOnline = null;
		try {
			URL onlineURL = new URL(
					"https://raw.githubusercontent.com/LucyBean/LucyGame/master/build_info.properties");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(onlineURL.openStream()));
			vOnline = getVersionFromReader(br);
			br.close();
		} catch (IOException e) {
			System.err.println("Unable to find version of online demo.");
		}

		return vOnline;
	}

	/**
	 * Finds the version of the game locally.
	 * 
	 * @return
	 */
	private static Version getLocalVersion() {
		Version vFile = null;
		try {
			File currentFile = new File(
					directoryName + File.separator + "build_info.properties");
			BufferedReader br = new BufferedReader(new FileReader(currentFile));
			vFile = getVersionFromReader(br);
			br.close();
		} catch (IOException e) {
			vFile = null;
			System.err.println("Unable to find version of local demo.");
		}

		return vFile;

	}

	private static Version getVersionFromReader(BufferedReader br)
			throws IOException {
		int major = 0;
		int minor = 0;
		int revision = 0;
		int build = 0;

		Pattern p = Pattern.compile("build\\.(\\w+)\\.number=(\\d+)");

		String nextLine = br.readLine();
		while (nextLine != null) {
			Matcher m = p.matcher(nextLine);
			if (m.matches()) {
				String type = m.group(1);
				int val = Integer.parseInt(m.group(2));

				if (type.equals("major"))
					major = val;
				if (type.equals("minor"))
					minor = val;
				if (type.equals("revision"))
					revision = val;
				if (type.equals("build"))
					build = val;
			}
			nextLine = br.readLine();
		}
		Version v = new Version(major, minor, revision, build);
		br.close();
		return v;
	}
}

class Version implements Comparable<Version> {
	int major;
	int minor;
	int revision;
	int build;

	public Version(int major, int minor, int revision, int build) {
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.build = build;
	}

	@Override
	public String toString() {
		return String.format("v %d.%d.%02d build %04d", major, minor, revision,
				build);
	}

	@Override
	public int compareTo(Version other) {
		// Return 0 if the versions are the same
		// Return <0 if other is newer
		// Return >0 if this is newer

		if (major != other.major) {
			return major - other.major;
		}
		if (minor != other.minor) {
			return minor - other.minor;
		}
		if (revision != other.revision) {
			return revision - other.revision;
		}
		return build - other.build;
	}
}
