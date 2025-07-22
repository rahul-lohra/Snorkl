import re

def get_version():
    with open("gradle.properties", "r") as f:
        for line in f:
            if line.startswith("VERSION_NAME="):
                return line.strip().split("=")[1]
    raise ValueError("VERSION_NAME not found")

def get_old_version_from_readme():
    with open("README.md", "r") as f:
        for line in f:
            if "rahul.lohra.snorkl:snorkl:" in line:
                match = re.search(r'rahul\.lohra\.snorkl:snorkl:([^"\)]+)', line)
                if match:
                    return match.group(1)
    raise ValueError("❌ Could not extract version from README.md")

def update_readme(old_version, new_version):
    with open("README.md", "r") as f:
        content = f.read()

    updated = content.replace(old_version, new_version)

    # update the version badge
    current_badge = old_version.split("-")[0]+ "--kotlin1.9"
    new_badge = new_version.split("-")[0] + "--kotlin1.9"
    updated = updated.replace(current_badge, new_badge)

    with open("README.md", "w") as f:
        f.write(updated)

    print(f"✅ README.md updated to version {new_version}")


if __name__ == "__main__":
    old_version = get_old_version_from_readme()
    new_version = get_version()    
    update_readme(old_version, new_version)
