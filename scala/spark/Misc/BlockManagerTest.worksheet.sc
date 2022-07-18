val rdd = spark.range(10).rdd

import org.apache.spark.SparkEnv
rdd.id
import  org.apache.spark.storage.BlockId
val blk = BlockId("rdd_5_1")
val blkVal = SparkEnv.get.blockManager.getLocalValues(blk).get.data.next
//no values in blockManager yet
// try to materialise RDD:
rdd.count
// val blkVal = SparkEnv.get.blockManager.getLocalValues(blk).get.data.next
// Still no values:
//java.util.NoSuchElementException: None.get
rdd.cache.count
// Now values are cached in blockManager
val blkVal = SparkEnv.get.blockManager.getLocalValues(blk).get.data.next
// blkVal: Any = 0

//collect blockIds for entire RDD:
val rdd5blkIds  = SparkEnv.get.blockManager.getMatchingBlockIds(_.asRDDId.get.rddId==5)
//get iterators for each block id
val rdd5Iterators = rdd5blkIds.map(x => SparkEnv.get.blockManager.getLocalValues(x))
//get values out of each iterable
val rdd5nonEmptyValues = rdd5Iterators.filter(_.get.data.hasNext).map(_.get.data.next)
