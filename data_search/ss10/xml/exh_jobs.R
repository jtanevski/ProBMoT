library(stringr)
# WD is temp/xml directory

x <- file("ssystem_exhaustive.xml", "r")
tx <- readLines(x)
close(x)

s <- file("../../ssystem_exhaustive.sh", "r")
ts <- readLines(s)
close(s)

r <- file("../../ssystem_exhaustive.xrsl", "r")
tr <- readLines(r)
close(r)

pind <- which(grepl("particles",tx))
mind <- which(grepl("maxev",tx))

tx[pind] <- str_replace(tx[pind],"[0-9]+","999")

for(mx in seq(1000,15000,by=1000)){
  tx[mind] <- str_replace(tx[mind],"[0-9]+",as.character(mx-1))
  x <- file(paste0("ssystem_exhaustive_",mx/1000,".xml"),open="w")
  writeLines(tx,x)
  close(x)
  
  s <- file(paste0("../../ssystem_exhaustive_",mx/1000,".sh"),open="w")
  ts[3] <- str_replace(ts[3],"ssystem_exhaustive_?[0-9]*\\.xml",paste0("ssystem_exhaustive_",mx/1000,".xml"))
  writeLines(ts,s)
  close(s)
  
  r <- file(paste0("../../ssystem_exhaustive_",mx/1000,".xrsl"),open="w")
  tr[2:3] <- str_replace(tr[2:3],"ssystem_exhaustive_?[0-9]*",paste0("ssystem_exhaustive_",mx/1000))
  writeLines(tr,r)
  close(r)
  
  
}