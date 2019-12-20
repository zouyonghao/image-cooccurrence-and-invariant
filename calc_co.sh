for i in {1..3}
do
echo G$i | xargs java -cp out/production/Project-4 CoOccurrence
done
