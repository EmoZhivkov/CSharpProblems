import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split, KFold
from sklearn.metrics import accuracy_score


class Id3:
    def fit(self, input, output):
        data = input.copy()
        data[output.name] = output
        self.tree = self.decision_tree(data, data, input.columns, output.name)

    def predict(self, input):
        samples = input.to_dict(orient='records')
        predictions = []

        for sample in samples:
            predictions.append(self.make_prediction(sample, self.tree, 1.0))

        return predictions

    def entropy(self, attribute_column):
        # get the unique values for the column
        values, counts = np.unique(attribute_column, return_counts=True)

        entropy_list = []

        for i in range(len(values)):
            probability = counts[i]/np.sum(counts)
            entropy_list.append(-probability*np.log2(probability))

        total_entropy = np.sum(entropy_list)

        return total_entropy

    def information_gain(self, data, feature_attribute_name, target_attribute_name):
        # total entropy of given subset
        total_entropy = self.entropy(data[target_attribute_name])

        # find unique values and their frequency counts for the attribute to be split
        values, counts = np.unique(data[feature_attribute_name], return_counts=True)

        weighted_entropy_list = []

        for i in range(len(values)):
            subset_probability = counts[i]/np.sum(counts)
            subset_entropy = self.entropy(data.where(data[feature_attribute_name]==values[i]).dropna()[target_attribute_name])
            weighted_entropy_list.append(subset_probability*subset_entropy)

        total_weighted_entropy = np.sum(weighted_entropy_list)

        information_gain = total_entropy - total_weighted_entropy

        return information_gain

    def decision_tree(self, data, original_data, feature_attribute_names, target_attribute_name, parent_node_class=None):
        # if data is pure, return the majority class of subset
        unique_classes = np.unique(data[target_attribute_name])
        if len(unique_classes) <= 1:
            return unique_classes[0]

        # if subset is empty, ie. no samples, return majority class of original data
        elif len(data) == 0:
            majority_class_index = np.argmax(np.unique(original_data[target_attribute_name], return_counts=True)[1])
            return np.unique(original_data[target_attribute_name])[majority_class_index]

        # if data set contains no features to train with, return parent node class
        elif len(feature_attribute_names) == 0:
            return parent_node_class

        # if none of the above are true, construct a branch:
        else:
            # determine parent node class of current branch
            majority_class_index = np.argmax(np.unique(data[target_attribute_name], return_counts=True)[1])
            parent_node_class = unique_classes[majority_class_index]

            # determine information gain values for each feature
            # choose feature which best splits the data, ie. highest value
            ig_values = [self.information_gain(data, feature, target_attribute_name) for feature in feature_attribute_names]
            best_feature_index = np.argmax(ig_values)
            best_feature = feature_attribute_names[best_feature_index]

            # create tree structure, empty at first
            tree = {best_feature: {}}

            # remove best feature from available features, it will become the parent node
            feature_attribute_names = [i for i in feature_attribute_names if i != best_feature]

            # create nodes under parent node
            parent_attribute_values = np.unique(data[best_feature])
            for value in parent_attribute_values:
                sub_data = data.where(data[best_feature] == value).dropna()

                # call the algorithm recursively
                subtree = self.decision_tree(sub_data, original_data, feature_attribute_names, target_attribute_name, parent_node_class)

                # add subtree to original tree
                tree[best_feature][value] = subtree

            return tree

    def make_prediction(self, sample, tree, default=1):
        for attribute in list(sample.keys()):
            if attribute in list(tree.keys()):
                try:
                    result = tree[attribute][sample[attribute]]
                except:
                    return default

                result = tree[attribute][sample[attribute]]

                # if more attributes exist within result, recursively find best result
                if isinstance(result, dict):
                    return self.make_prediction(sample, result)
                else:
                    return result


def main():
    data_path = "breast-cancer.data"
    df = pd.read_csv(data_path)

    # give a name to the columns for easier work
    columns = ['has_cancer','age','menopause','tumor-size','inv-nodes','node-caps','deg-malig','breast','breast-quad','irradiat']
    df.columns = columns

    # remove all rows with missing values
    df = df.replace("?", np.nan)
    df = df.dropna()

    X = df.drop(columns="has_cancer")
    y = df["has_cancer"]

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33)

    model = Id3()
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    print(accuracy_score(y_test, y_pred))


if __name__ == "__main__":
    main()