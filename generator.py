"""Generates a XML file with petitions for a IO scheduler simulation"""
import random


def generate_list(num_bytes, pets, time, max_bl):
    """Generates a list with tuples representing petitions
    returns a tuple of the form: (priority, time, [blocks], type)
    """
    tuples = []
    disk_bl = num_bytes / 4096
    for _ in range(pets):
        priority = random.randint(0, 2)
        if priority == 0:
            priority = 'RT'
        elif priority == 1:
            priority = 'BE'
        else:
            priority = 'IDLE'

        time = random.randint(0, time)
        num_bl = random.randint(1, max_bl)
        blocks = []
        for _ in range(num_bl):
            blocks.append(random.randint(0, disk_bl))

        kind = random.randint(0, 1)
        if kind == 0:
            kind = 'R'
        else:
            kind = 'W'

        petition = (priority, time, blocks, kind)

        tuples.append(petition)

    return tuples


def generate_xml(tuples):
    """Generates a string with the XML file to be printed
    Receives a list of tuples with the form: (priority, time, [blocks], type)
    """
    xml = "<xml>\n"

    def spaces(level):
        """Creates an indentation level"""
        return ' ' * level * 4

    def pet_tag(prio, time, blocks, kind, level):
        """Generates a tag containing the information passed"""
        pet = spaces(level) + "<petition>\n"
        pet += prio_tag(prio, level + 1) + '\n'
        pet += time_tag(time, level + 1) + '\n'
        pet += type_tag(kind, level + 1) + '\n'
        pet += list_tag(blocks, level + 1) + '\n'
        pet += spaces(level) + "</petition>"

        return pet

    def prio_tag(info, level):
        """Generates a tag containing the information passed"""
        return spaces(level) + "<priority>" + str(info) + "</priority>"

    def time_tag(info, level):
        """Generates a tag containing the information passed"""
        return spaces(level) + "<time>" + str(info) + "</time>"

    def type_tag(info, level):
        """Generates a tag containing the information passed"""
        return spaces(level) + "<type>" + str(info) + "</type>"

    def block_tag(info, level):
        """Generates a tag containing the information passed"""
        return spaces(level) + "<block>" + str(info) + "</block>"

    def list_tag(blocks, level):
        """Generates a tag containing the information passed"""
        string = spaces(level) + "<list>\n"
        for elem in blocks:
            string += block_tag(elem, level + 1) + '\n'

        string += spaces(level) + "</list>"
        return string

    for pet in tuples:
        xml += pet_tag(pet[0], pet[1], pet[2], pet[3], 1) + '\n'

    xml += "</xml>\n"
    return xml


###############################################################################

def main(argv = None):
    """Main function
    Only to be called if this is the main module (not imported in another one)
    """
    import sys      # argv, exit

    if argv is None:
        argv = sys.argv

    if len(argv) != 5:
        print "USAGE: python generador.py <bytes> <petitions>",
        print "<time> <maximum blocks>"
        return

    num_bytes = argv[1]
    petitions = argv[2]
    time = argv[3]
    max_blocks = argv[4]

    if not num_bytes.isdigit():
        print "USAGE: <bytes> must be a positive integer"

    if not petitions.isdigit():
        print "USAGE: <petitions> must be a positive integer"

    if not time.isdigit():
        print "USAGE: <time> must be a positive integer"

    if not time.isdigit():
        print "USAGE: <maximum blocks> must be a positive integer, ",
        print "indicating maximum number of blocks to be asked by a petition"

    num_bytes = int(num_bytes)
    petitions = int(petitions)
    time = int(time)
    max_blocks = int(max_blocks)

    tuples = generate_list(num_bytes, petitions, time, max_blocks)
    xml = generate_xml(tuples)

    print xml

# If this is the module running
if __name__ == "__main__":
    main()
