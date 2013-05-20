import random

# Generates a list with tuples representing petitions
# (priority, time, [blocks], type)
def generate_list(bytes, pets, time, max_bl):
    list    = []
    disk_bl = bytes / 4096
    for _ in range(pets):
        priority = random.randint(0, 2)
        if priority == 0:
            priority = 'RT'
        elif priority == 1:
            priority = 'BE'
        else:
            priority = 'IDLE'

        time     = random.randint(0, time)
        num_bl   = random.randint(1,max_bl)
        blocks   = []
        for _ in range(num_bl):
            blocks.append(random.randint(0, disk_bl))

        type     = random.randint(0,1)
        if type == 0:
            type = 'R'
        else:
            type = 'W'

        petition = (priority, time, blocks, type)

        list.append(petition)

    return list

# Receives a list of tuples
# (priority, time, [blocks], type)
def generate_xml(list, bytes):
    xml      = "<xml>\n"
    disk_tag = "<disk>" + str(bytes) + "</disk>\n"

    def spaces(level):
        return ' ' * level * 4

    def pet_tag(prio, time, blocks, type, level):
        pet =  spaces(level) + "<petition>\n"
        pet += prio_tag(prio, level + 1) + '\n'
        pet += time_tag(time, level + 1) + '\n'
        pet += type_tag(type, level + 1) + '\n'
        pet += list_tag(blocks, level + 1) + '\n'
        pet += spaces(level) + "</petition>"

        return pet

    def prio_tag(info, level):
        return spaces(level) + "<priority>" + str(info) + "</priority>"

    def time_tag(info, level):
        return spaces(level) + "<time>" + str(info) + "</time>"

    def type_tag(info, level):
        return spaces(level) + "<type>" + str(info) + "</type>"

    def block_tag(info, level):
        return spaces(level) + "<block>" + str(info) + "</block>"

    def list_tag(blocks, level):
        string = spaces(level) + "<list>\n"
        for b in blocks:
            string += block_tag(b, level + 1) + '\n'

        string += spaces(level) + "</list>"
        return string

    for pet in list:
        xml += pet_tag(pet[0], pet[1], pet[2], pet[3], 1) + '\n'

    xml += "</xml>\n"
    return xml


###############################################################################

# Only to be called if this is the main module (not imported in another one)
def main(argv = None):
    import sys      # argv, exit

    if argv is None:
        argv = sys.argv

    if len(argv) != 5:
        print "USAGE: python generador.py <bytes> <petitions>",
        print "<time> <maximum blocks>"
        return

    bytes      = argv[1]
    petitions  = argv[2]
    time       = argv[3]
    max_blocks = argv[4]

    if not bytes.isdigit():
        print "USAGE: <bytes> must be a positive integer"

    if not petitions.isdigit():
        print "USAGE: <petitions> must be a positive integer"

    if not time.isdigit():
        print "USAGE: <time> must be a positive integer"

    if not time.isdigit():
        print "USAGE: <maximum blocks> must be a positive integer,",
        print "indicating maximum number of blocks to be asked by a petition"

    bytes      = int(bytes)
    petitions  = int(petitions)
    time       = int(time)
    max_blocks = int(max_blocks)

    list = generate_list(bytes, petitions, time, max_blocks)
    xml = generate_xml(list, bytes)

    print xml

# If this is the module running
if __name__ == "__main__":
    main()
