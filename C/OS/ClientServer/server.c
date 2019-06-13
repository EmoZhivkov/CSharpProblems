#include <sys/types.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <semaphore.h>
#include <sys/mman.h>
#include "comm.h"

int main(int argc, char **argv) {
    if (argc != 2) {
        error("The server takes exactly one argument. Namely the file it reads from");
    }

    initValidAccount();

    uint32_t bank[8];
    int fd;

    char *fileName = argv[1];
    if (!doesFileExist(fileName)) {
        fd = open(fileName, O_CREAT | O_RDWR, 0666);
        for (int i = 0; i < 8; ++i) {
            uint32_t temp = 0;
            uint32_t size = write(fd, &temp, 4);
            if (size != 4) {
                error("Could not write the numbers to the file.");
            }
            bank[i] = 0;
        }
    } else {
        fd = open(fileName, O_RDWR);
        int bytesRead = read(fd, &bank, 8*4);
        if (bytesRead != 8*4)
        {
            error("Could not read the numbers from the file");
        }
        
    }

    char *shared_mem_ptr;
    sem_t *mutex_sem,*spool_signal_sem,*take_from_bank_sem;
    int fd_shm;
    char buff[256];

    //  mutual exclusion semaphore, mutex_sem with an initial value 0.
    if ((mutex_sem = sem_open(SEM_MUTEX_NAME, O_CREAT, 0660, 0)) == SEM_FAILED)
        error("sem_open");

    // Get shared memory
    if ((fd_shm = shm_open(SHARED_MEM_NAME, O_RDWR | O_CREAT, 0660)) == -1)
        error("shm_open");

    if (ftruncate(fd_shm, 256) < 0) {
        error("Could not set the shared memory size.");
    }

    shared_mem_ptr = mmap(NULL, 256, PROT_READ | PROT_WRITE, MAP_SHARED, fd_shm, 0);

    // counting semaphore, indicating the number of strings to be printed. Initial value = 0
    if ((spool_signal_sem = sem_open(SEM_SPOOL_SIGNAL_NAME, O_CREAT, 0660, 0)) == SEM_FAILED)
        error("sem_open");

    if ((take_from_bank_sem = sem_open(SEM_BANK_NAME, O_CREAT, 0660, 0)) == SEM_FAILED)
        error("sem_open");

    // Initialization complete; now we can set mutex semaphore as 1 to
    // indicate shared memory segment is available
    if (sem_post(mutex_sem) == -1)
        error("sem_post: mutex_sem");


// ne rabotqt davaneto na otricatelno chislo + sled towa wsichki dawat invalid account
    while (1) {
        // Is there a string to print? P (spool_signal_sem);
        if (sem_wait(spool_signal_sem) == -1)
            error("sem_wait: spool_signal_sem");

        strcpy (buff, shared_mem_ptr);
        char account = buff[1];
        uint8_t indexForValid = (uint8_t)account;
        uint8_t index = account - 65;
        
        if (!isValidAccount[indexForValid]) {
            sprintf(buff, "%d", -1);
            strcpy(shared_mem_ptr, buff);


        if (sem_post(take_from_bank_sem) == -1)
            error("sem_post: take_from_bank_sem");

        continue;
        } else {
            sprintf(buff, "%d", bank[index]);
            strcpy(shared_mem_ptr, buff);
        }

        if (sem_post(take_from_bank_sem) == -1)
            error("sem_post: take_from_bank_sem");

        if (sem_wait(spool_signal_sem) == -1)
            error("sem_wait: spool_signal_sem");

        long mult = 1;
        if (shared_mem_ptr[0] == '-') {
            shared_mem_ptr++;
            mult = -1;
        } 
        
        long toAdd = strtol(shared_mem_ptr, (char **)NULL, 10);
        if (toAdd > UINT16_MAX) {
            sprintf(buff, "%d", -1);
            strcpy(shared_mem_ptr, buff);


        if (sem_post(take_from_bank_sem) == -1)
            error("sem_post: take_from_bank_sem");

        continue;
        } else {
            toAdd *= mult;
            if ((long)bank[index] + toAdd < 0 || (long)bank[index] + toAdd > UINT32_MAX) {
                sprintf(buff, "%d", -1);
                strcpy(shared_mem_ptr, buff);


        if (sem_post(take_from_bank_sem) == -1)
            error("sem_post: take_from_bank_sem");

        continue;
            } else {
                bank[index] = bank[index] + toAdd;
                sprintf(shared_mem_ptr, "%s", "success");
                lseek(fd, 0, SEEK_SET);
                int size = write(fd, &bank, 8*4);    
                if (size != 8*4) {
                    error("Failed to write to file");
                }
            }
        }
        
        if (sem_post(take_from_bank_sem) == -1)
            error("sem_post: take_from_bank_sem");
    }
}
